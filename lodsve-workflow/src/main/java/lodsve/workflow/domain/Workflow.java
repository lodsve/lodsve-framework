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

package lodsve.workflow.domain;

import lodsve.core.utils.ListUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

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
        FlowNode checkNode = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
            @Override
            public boolean judge(FlowNode target) {
                return name.equals(target.getNext());
            }
        });

        if (checkNode == null) {
            return node;
        } else {
            return findStartNode(checkNode, nodes);
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
