package message.workflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import message.workflow.enums.UrlType;
import message.workflow.api.HandlerInterceptor;

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
    @Column(length = 1000)
    private String conditional;
    @Column
    private String next;
    @Column
    private int nodeVersion;
    @Column
    private String interceptorBean;
    @Column
    private String interceptorClass;
    @Column
    private UrlType urlType;

    @Transient
    private FormUrl formUrl;
    @Transient
    private HandlerInterceptor interceptor;

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

    public String getConditional() {
        return conditional;
    }

    public void setConditional(String conditional) {
        this.conditional = conditional;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(int nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public String getInterceptorBean() {
        return interceptorBean;
    }

    public void setInterceptorBean(String interceptorBean) {
        this.interceptorBean = interceptorBean;
    }

    public String getInterceptorClass() {
        return interceptorClass;
    }

    public void setInterceptorClass(String interceptorClass) {
        this.interceptorClass = interceptorClass;
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

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }
}
