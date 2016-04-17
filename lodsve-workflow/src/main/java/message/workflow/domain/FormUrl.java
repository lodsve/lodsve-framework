package message.workflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import message.workflow.enums.UrlType;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午2:42
 */
@Entity
@Table(name = "t_form_url")
public class FormUrl {
    @Id
    private Long id;
    @Column
    private String url;
    @Column
    private UrlType type;
    @Column
    private Long workFlowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlType getType() {
        return type;
    }

    public void setType(UrlType type) {
        this.type = type;
    }

    public Long getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(Long workFlowId) {
        this.workFlowId = workFlowId;
    }
}
