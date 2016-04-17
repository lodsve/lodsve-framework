package message.workflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import message.workflow.enums.UrlType;

/**
 * 工作流节点.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 下午1:43
 */
@Entity
@Table(name = "t_flow_node")
public class FlowNode {
    @Id
    private Long id;
    @Column
    private Long flowId;
    @Column
    private String title;
    @Column
    private String name;
    @Column
    private String method;
    @Column(length = 1000)
    private String conditional;
    @Column(name = "next")
    private String to;
    @Column(name = "node_version")
    private int version;
    @Column
    private UrlType urlType;

    private FormUrl formUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getConditional() {
        return conditional;
    }

    public void setConditional(String conditional) {
        this.conditional = conditional;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    public FormUrl getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(FormUrl formUrl) {
        this.formUrl = formUrl;
    }
}
