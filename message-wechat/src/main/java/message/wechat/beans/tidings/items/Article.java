package message.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 文章.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:23
 */
@ApiModel(description = "文章")
public class Article {
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    @ApiModelProperty(value = "描述", required = true)
    private String description;
    @ApiModelProperty(value = "url", required = true)
    private String url;
    @ApiModelProperty(value = "图片url", required = true)
    @JsonProperty("picurl")
    private String picUrl;

    @ApiModelProperty(value = "标题", required = true)
    public String getTitle() {
        return title;
    }

    @ApiModelProperty(value = "标题", required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "描述", required = true)
    public String getDescription() {
        return description;
    }

    @ApiModelProperty(value = "描述", required = true)
    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "url", required = true)
    public String getUrl() {
        return url;
    }

    @ApiModelProperty(value = "url", required = true)
    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(value = "图片url", required = true)
    public String getPicUrl() {
        return picUrl;
    }

    @ApiModelProperty(value = "图片url", required = true)
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
