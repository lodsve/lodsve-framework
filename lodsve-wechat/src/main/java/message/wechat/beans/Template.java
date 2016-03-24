package message.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模板.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/4 下午1:43
 */
@ApiModel(description = "模板")
public class Template {
    /**
     * 模板ID
     */
    @ApiModelProperty(value = "模板ID", required = true)
    @JsonProperty("template_id")
    private String templateId;
    /**
     * 模板标题
     */
    @ApiModelProperty(value = "模板标题", required = true)
    private String title;
    /**
     * 模板所属行业的一级行业
     */
    @ApiModelProperty(value = "模板所属行业的一级行业", required = true)
    @JsonProperty("primary_industry")
    private String primary;
    /**
     * 模板所属行业的二级行业
     */
    @ApiModelProperty(value = "模板所属行业的二级行业", required = true)
    @JsonProperty("deputy_industry")
    private String deputy;
    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容", required = true)
    private String content;
    /**
     * 模板示例
     */
    @ApiModelProperty(value = "模板示例", required = true)
    private String example;

    @ApiModelProperty(value = "模板ID", required = true)
    public String getTemplateId() {
        return templateId;
    }

    @ApiModelProperty(value = "模板ID", required = true)
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @ApiModelProperty(value = "模板标题", required = true)
    public String getTitle() {
        return title;
    }

    @ApiModelProperty(value = "模板标题", required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "模板所属行业的一级行业", required = true)
    public String getPrimary() {
        return primary;
    }

    @ApiModelProperty(value = "模板所属行业的一级行业", required = true)
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @ApiModelProperty(value = "模板所属行业的二级行业", required = true)
    public String getDeputy() {
        return deputy;
    }

    @ApiModelProperty(value = "模板所属行业的二级行业", required = true)
    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    @ApiModelProperty(value = "模板内容", required = true)
    public String getContent() {
        return content;
    }

    @ApiModelProperty(value = "模板内容", required = true)
    public void setContent(String content) {
        this.content = content;
    }

    @ApiModelProperty(value = "模板示例", required = true)
    public String getExample() {
        return example;
    }

    @ApiModelProperty(value = "模板示例", required = true)
    public void setExample(String example) {
        this.example = example;
    }
}
