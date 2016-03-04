package message.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 模板.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/4 下午1:43
 */
public class Template {
    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;
    /**
     * 模板标题
     */
    private String title;
    /**
     * 模板所属行业的一级行业
     */
    @JsonProperty("primary_industry")
    private String primary;
    /**
     * 模板所属行业的二级行业
     */
    @JsonProperty("deputy_industry")
    private String deputy;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 模板示例
     */
    private String example;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
