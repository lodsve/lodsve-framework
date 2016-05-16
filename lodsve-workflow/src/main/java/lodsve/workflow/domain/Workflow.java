package lodsve.workflow.domain;

import java.util.List;
import lodsve.base.utils.ListUtils;
import org.apache.commons.collections.CollectionUtils;

/**
 * 工作流.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 下午1:43
 */
public class Workflow {
    private Long id;
    private String title;
    private String name;
    private String domain;
    private int version = 0;
    private String xml;
    private String xmlMd;

    private List<FlowNode> nodes;
    private Class<?> domainClass;
    private FlowNode startNode;
    private FlowNode endNode;
    private List<FormUrl> formUrls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getXmlMd() {
        return xmlMd;
    }

    public void setXmlMd(String xmlMd) {
        this.xmlMd = xmlMd;
    }

    public List<FlowNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<FlowNode> nodes) {
        this.nodes = nodes;

        // 结束节点
        FlowNode endNode = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
            @Override
            public boolean judge(FlowNode target) {
                return "end".equals(target.getNext());
            }
        });
        setEndNode(endNode);

        // 开始节点
        FlowNode startNode = findStartNode(endNode, nodes);
        setStartNode(startNode);
    }

    private FlowNode findStartNode(FlowNode node, List<FlowNode> nodes) {
        if (CollectionUtils.isEmpty(nodes) || node == null) {
            return null;
        }

        final String name = node.getName();
        FlowNode _node = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
            @Override
            public boolean judge(FlowNode target) {
                return name.equals(target.getNext());
            }
        });

        if (_node == null) {
            return node;
        } else {
            return findStartNode(_node, nodes);
        }
    }

    public Class<?> getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class<?> domainClass) {
        this.domainClass = domainClass;
    }

    public FlowNode getStartNode() {
        return startNode;
    }

    public void setStartNode(FlowNode startNode) {
        this.startNode = startNode;
    }

    public FlowNode getEndNode() {
        return endNode;
    }

    public void setEndNode(FlowNode endNode) {
        this.endNode = endNode;
    }

    public List<FormUrl> getFormUrls() {
        return formUrls;
    }

    public void setFormUrls(List<FormUrl> formUrls) {
        this.formUrls = formUrls;
    }
}
