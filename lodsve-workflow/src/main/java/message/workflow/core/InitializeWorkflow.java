package message.workflow.core;

import message.base.utils.EncryptUtils;
import message.base.utils.ListUtils;
import message.base.utils.ObjectUtils;
import message.base.utils.StringUtils;
import message.base.utils.XmlUtils;
import message.workflow.Constants;
import message.workflow.domain.FlowNode;
import message.workflow.domain.Workflow;
import message.workflow.service.WorkflowLocalStorage;
import message.workflow.service.WorkflowService;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工作流.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 下午1:43
 */
@Component
public class InitializeWorkflow implements ApplicationListener<ContextRefreshedEvent> {
    private List<Resource> resources = new ArrayList<>();

    @Autowired
    private WorkflowService workflowService;

    private void init() throws Exception {
        for (Resource resource : resources) {
            Document document = XmlUtils.parseXML(resource.getInputStream());

            // 获取根元素
            Element root = document.getRootElement();
            Workflow workflow = initWorkflow(resource, document, root);

            if (workflow != null && CollectionUtils.isNotEmpty(workflow.getNodes())) {
                // 流程未发生变化
                WorkflowLocalStorage.store(workflow);
                continue;
            }

            if (workflow == null) {
                // 可能是出问题了
                continue;
            }

            // 获取根元素下的所有node节点
            List<Element> children = XmlUtils.getChildren(root, Constants.TAG_NODE);
            List<FlowNode> nodes = initFlowNode(workflow, children);
            workflow.setNodes(nodes);

            WorkflowLocalStorage.store(workflow);
        }
    }

    private List<FlowNode> initFlowNode(Workflow workflow, List<Element> children) {
        List<FlowNode> nodes = new ArrayList<>(children.size());
        for (Element child : children) {
            String title = XmlUtils.getElementAttr(child, Constants.ATTR_TITLE);
            String name = XmlUtils.getElementAttr(child, Constants.ATTR_NAME);

            String method = XmlUtils.getAttrValue(child, Constants.TAG_METHOD + ":" + Constants.ATTR_NAME);
            // 校验方法是否存在
            checkHandlerMethod(workflow.getName(), name, workflow.getHandlerClass(), method);
            String to = XmlUtils.getAttrValue(child, Constants.TAG_TO + ":" + Constants.ATTR_NODE);
            String roles = XmlUtils.getElementBody(XmlUtils.getChild(child, Constants.TAG_CONDITIONAL));

            FlowNode node = new FlowNode();
            node.setTitle(title);
            node.setName(name);
            node.setMethod(method);
            node.setTo(to);
            node.setFlowId(workflow.getId());
            node.setVersion(workflow.getVersion());
            node.setRoles(roles);

            nodes.add(node);
        }

        checkNode(nodes);
        return workflowService.saveFlowNodes(nodes);
    }

    private void checkNode(List<FlowNode> nodes) {
        for (FlowNode node : nodes) {
            final String name = node.getName();
            final String to = node.getTo();

            // 校验code是否唯一
            List<FlowNode> _nodes = ListUtils.findMore(nodes, new ListUtils.Decide<FlowNode>() {
                @Override
                public boolean judge(FlowNode target) {
                    return name.equals(target.getName());
                }
            });

            if (_nodes.size() > 1) {
                throw new RuntimeException("节点【" + node.getName() + "】的code【" + name + "】存在重复！");
            }

            // 检查to节点是否存在
            FlowNode _node = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
                @Override
                public boolean judge(FlowNode target) {
                    return to.equals(target.getName()) || "end".equals(to);
                }
            });

            if (_node == null) {
                throw new RuntimeException("节点【" + node.getName() + "】的下一节点【" + name + "】不存在，请检查！");
            }
        }
    }

    private Workflow initWorkflow(Resource resource, Document document, Element root) {
        String title = XmlUtils.getElementAttr(root, Constants.ATTR_TITLE);
        String name = XmlUtils.getElementAttr(root, Constants.ATTR_NAME);
        String handler = XmlUtils.getElementAttr(root, Constants.ATTR_HANDLER);
        String domain = XmlUtils.getElementAttr(root, Constants.ATTR_DOMAIN);

        int version = 1;
        // 判断流程是否发生变化，如果发生变化，则版本号增加1
        String xmlMd5 = getFileMD5(resource);
        Workflow lastestWorkflow = workflowService.loadLatestWorkflow(name);
        if (lastestWorkflow != null) {
            if (xmlMd5.equals(lastestWorkflow.getXmlMd5())) {
                return lastestWorkflow;
            }
            // 版本号递增
            version = lastestWorkflow.getVersion() + 1;
        }

        // 检查处理类是否正确
        Class<?> handlerClass = null;
        if (StringUtils.isNotBlank(handler)) {
            handlerClass = checkClass(handler, String.format("解析流程处理类出现问题！流程名：%s, 处理类：%s", name, handler));
        }
        // 检查domain
        Class<?> domainClass = checkClass(domain, String.format("解析流程domain类出现问题！流程名：%s, domain类：%s", name, domain));

        String xmlContent = XmlUtils.parseXMLToString(document);

        Workflow workflow = new Workflow();

        workflow.setTitle(title);
        workflow.setName(name);
        workflow.setVersion(version);
        workflow.setHandler(handler);
        workflow.setXml(xmlContent);
        workflow.setXmlMd5(xmlMd5);
        workflow.setHandlerClass(handlerClass);
        workflow.setDomain(domain);
        workflow.setDomainClass(domainClass);

        // 保存(每次都是增加，修改版本号)
        workflowService.saveWorkflow(workflow);
        return workflow;
    }

    private String getFileMD5(Resource resource) {
        File file;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            file = null;
        }


        String xmlMd5 = "";
        if (file != null) {
            xmlMd5 = EncryptUtils.getFileMD5String(file);
        }

        return xmlMd5;
    }

    private Class<?> checkClass(String className, String errorMsg) {
        Class<?> clazz;
        try {
            // 判断是否存在
            clazz = ClassUtils.forName(className, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(errorMsg, e);
        }

        return clazz;
    }

    private void checkHandlerMethod(String flowName, String nodeName, Class<?> handlerCLass, String method) {
        boolean isContains = false;
        try {
            isContains = Arrays.asList(ObjectUtils.getMethodNames(handlerCLass.newInstance(), false)).contains(method);
        } catch (Exception e) {

        }

        if (!isContains) {
            throw new RuntimeException(String.format("流程【%s】的节点【%s】的处理类【%s】不包含方法【%s】！", flowName, nodeName, handlerCLass.getName(), method));
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] _resources;
        try {
            _resources = loader.getResources(Constants.DEFAULT_WORKFLOW_XML_PATH);
        } catch (IOException e) {
            throw new RuntimeException("获取工作流配置文件列表失败", e);
        }

        resources.addAll(Arrays.asList(_resources));

        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException("流程配置文件解析发生错误", e);
        }
    }
}
