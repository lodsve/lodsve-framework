/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.workflow.core;

import lodsve.core.utils.EncryptUtils;
import lodsve.core.utils.ListUtils;
import lodsve.core.utils.StringUtils;
import lodsve.core.utils.XmlUtils;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.workflow.Constants;
import lodsve.workflow.api.HandlerInterceptor;
import lodsve.workflow.domain.FlowNode;
import lodsve.workflow.domain.FormUrl;
import lodsve.workflow.domain.Workflow;
import lodsve.workflow.enums.UrlType;
import lodsve.workflow.exception.InitializeWorkflowException;
import lodsve.workflow.repository.FlowNodeRepository;
import lodsve.workflow.repository.FormUrlRepository;
import lodsve.workflow.repository.WorkflowLocalStorage;
import lodsve.workflow.repository.WorkflowRepository;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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

    private ContextRefreshedEvent applicationReadyEvent;

    @Autowired
    private WorkflowRepository workflowRepository;
    @Autowired
    private FlowNodeRepository flowNodeRepository;
    @Autowired
    private FormUrlRepository formUrlRepository;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private void init() throws Exception {
        for (Resource resource : resources) {
            Document document = XmlUtils.parseXML(resource.getInputStream());

            // 获取根元素
            Element root = document.getRootElement();
            initWorkflow(resource, document, root);
        }
    }

    private List<FormUrl> resovleUrls(List<Element> urls, Workflow workflow) {
        // 有且仅有一个
        Assert.isTrue(CollectionUtils.isNotEmpty(urls));

        Element element = urls.get(0);

        FormUrl updateFormUrl = new FormUrl();
        FormUrl viewFormUrl = new FormUrl();

        Long id = snowflakeIdGenerator.nextId("");
        updateFormUrl.setId(id);
        updateFormUrl.setUrlType(UrlType.UPDATE);
        updateFormUrl.setUrl(XmlUtils.getAttrValue(element, Constants.TAG_UPDATE_URL + ":" + Constants.ATTR_URL));
        updateFormUrl.setFlowId(workflow.getId());

        id = snowflakeIdGenerator.nextId("");
        viewFormUrl.setId(id);
        viewFormUrl.setUrlType(UrlType.VIEW);
        viewFormUrl.setUrl(XmlUtils.getAttrValue(element, Constants.TAG_VIEW_URL + ":" + Constants.ATTR_URL));
        viewFormUrl.setFlowId(workflow.getId());

        return Arrays.asList(updateFormUrl, viewFormUrl);
    }

    private List<FlowNode> resovleFlowNode(Workflow workflow, List<Element> children, List<FormUrl> urls) {
        List<FlowNode> nodes = new ArrayList<>(children.size());
        for (Element child : children) {
            String title = XmlUtils.getElementAttr(child, Constants.ATTR_TITLE);
            String name = XmlUtils.getElementAttr(child, Constants.ATTR_NAME);
            String type = XmlUtils.getElementAttr(child, Constants.ATTR_TYPE);

            String to = XmlUtils.getAttrValue(child, Constants.TAG_TO + ":" + Constants.ATTR_NODE);
            String conditional = XmlUtils.getElementBody(XmlUtils.getChild(child, Constants.TAG_CONDITIONAL));

            FlowNode node = new FlowNode();
            if (XmlUtils.hasChildNode(child, Constants.TAG_INTERCEPTOR)) {
                String bean = XmlUtils.getAttrValue(child, Constants.TAG_INTERCEPTOR + ":" + Constants.ATTR_BEAN);
                String clazz = XmlUtils.getAttrValue(child, Constants.TAG_INTERCEPTOR + ":" + Constants.ATTR_CLASS);

                // 检查处理类是否正确
                HandlerInterceptor handler = checkHandler(workflow.getTitle(), bean, clazz);
                node.setInterceptorBean(bean);
                node.setInterceptorClass(clazz);
                node.setInterceptor(handler);
            }

            Long id = snowflakeIdGenerator.nextId("");
            node.setId(id);
            node.setTitle(title);
            node.setName(name);
            node.setNext(to);
            node.setFlowId(workflow.getId());
            node.setNodeVersion(workflow.getVersion());
            node.setConditional(conditional);
            final UrlType urlType = UrlType.eval(type);
            node.setUrlType(urlType);
            node.setFormUrl(ListUtils.findOne(urls, new ListUtils.Decide<FormUrl>() {
                @Override
                public boolean judge(FormUrl target) {
                    return urlType == target.getUrlType();
                }
            }));

            nodes.add(node);
        }

        checkNode(nodes);
        return nodes;
    }

    private HandlerInterceptor checkHandler(String title, String bean, String clazz) {
        if (StringUtils.isNotBlank(bean)) {
            // 判断bean
            Object source;
            try {
                source = applicationReadyEvent.getApplicationContext().getBean(bean);
            } catch (BeansException e) {
                throw new InitializeWorkflowException(108001, String.format("解析流程处理类出现问题！流程名：%s, 处理类：%s", title, bean), title, bean);
            }
            if (source == null || source.getClass().isAssignableFrom(HandlerInterceptor.class)) {
                throw new InitializeWorkflowException(108001, String.format("解析流程处理类出现问题！流程名：%s, 处理类：%s", title, bean), title, bean);
            }

            return (HandlerInterceptor) source;
        }
        if (StringUtils.isNotBlank(clazz)) {
            // 判断class
            Class<?> clazzForName;
            try {
                clazzForName = ClassUtils.forName(clazz, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new InitializeWorkflowException(108001, String.format("解析流程处理类出现问题！流程名：%s, 处理类：%s", title, clazz), title, bean);
            }

            return (HandlerInterceptor) BeanUtils.instantiate(clazzForName);
        }

        throw new InitializeWorkflowException(108002, "handler节点属性bean或者class必须二选一!");
    }

    private void checkNode(List<FlowNode> nodes) {
        for (FlowNode node : nodes) {
            final String name = node.getName();
            final String to = node.getNext();

            // 校验code是否唯一
            List<FlowNode> nodeList = ListUtils.findMore(nodes, new ListUtils.Decide<FlowNode>() {
                @Override
                public boolean judge(FlowNode target) {
                    return name.equals(target.getName());
                }
            });

            if (nodeList.size() > 1) {
                throw new InitializeWorkflowException(108003, "节点【" + node.getName() + "】的code【" + name + "】存在重复！", node.getName(), name);
            }

            // 检查to节点是否存在
            FlowNode checkNode = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
                @Override
                public boolean judge(FlowNode target) {
                    return to.equals(target.getName()) || "end".equals(to);
                }
            });

            if (checkNode == null) {
                throw new InitializeWorkflowException(108004, "节点【" + node.getName() + "】的下一节点【" + name + "】不存在，请检查！", node.getName(), name);
            }
        }
    }

    private void initWorkflow(Resource resource, Document document, Element root) {
        String title = XmlUtils.getElementAttr(root, Constants.ATTR_TITLE);
        String name = XmlUtils.getElementAttr(root, Constants.ATTR_NAME);
        String domain = XmlUtils.getElementAttr(root, Constants.ATTR_DOMAIN);

        Workflow lastestWorkflow = workflowRepository.loadLatest(name);
        boolean needSave = true;
        int version = lastestWorkflow == null ? 0 : lastestWorkflow.getVersion();

        // 检查domain
        Class<?> domainClass = checkClass(domain, String.format("解析流程domain类出现问题！流程名：%s, domain类：%s", name, domain));

        String xmlContent = XmlUtils.parseXMLToString(document);

        Workflow workflow;
        if (lastestWorkflow != null && isSameVersion(name, resource)) {
            // 同一流程,但是版本不同,版本号递增
            workflow = lastestWorkflow;
            needSave = false;
        } else {
            workflow = new Workflow();

            workflow.setTitle(title);
            workflow.setName(name);
            workflow.setVersion(++version);
            workflow.setXml(xmlContent);
            workflow.setXmlMd(getFileMD5(resource));
            workflow.setDomain(domain);

            workflowRepository.save(workflow);
        }

        // 处理url节点
        List<Element> urlEles = XmlUtils.getChildren(root, Constants.TAG_URLS);
        List<FormUrl> urls = resovleUrls(urlEles, workflow);

        if (needSave) {
            formUrlRepository.saveFormUrls(urls);
        }

        // 获取根元素下的所有node节点
        List<Element> children = XmlUtils.getChildren(root, Constants.TAG_NODE);
        List<FlowNode> nodes = resovleFlowNode(workflow, children, urls);

        if (needSave) {
            flowNodeRepository.saveFlowNodes(nodes);
        }

        workflow.setNodes(nodes);
        workflow.setFormUrls(urls);
        workflow.setDomainClass(domainClass);
        WorkflowLocalStorage.store(workflow);
    }

    private boolean isSameVersion(String flowName, Resource resource) {
        // 判断流程是否发生变化，如果发生变化，则版本号增加1
        String xmlMd5 = getFileMD5(resource);
        Workflow lastestWorkflow = workflowRepository.loadLatest(flowName);

        return lastestWorkflow != null && xmlMd5.equals(lastestWorkflow.getXmlMd());

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
            xmlMd5 = EncryptUtils.getFileMD5(file);
        }

        return xmlMd5;
    }

    private Class<?> checkClass(String className, String errorMsg) {
        Class<?> clazz;
        try {
            // 判断是否存在
            clazz = ClassUtils.forName(className, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new InitializeWorkflowException(108005, errorMsg);
        }

        return clazz;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {
        this.applicationReadyEvent = applicationReadyEvent;

        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] resourceArray;
        try {
            resourceArray = loader.getResources(Constants.DEFAULT_WORKFLOW_XML_PATH);
        } catch (IOException e) {
            throw new InitializeWorkflowException(108006, "获取工作流配置文件列表失败");
        }

        resources.addAll(Arrays.asList(resourceArray));

        try {
            init();
        } catch (Exception e) {
            throw new InitializeWorkflowException(108007, "流程配置文件解析发生错误");
        }
    }
}
