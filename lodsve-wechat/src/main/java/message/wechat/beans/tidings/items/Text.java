package message.wechat.beans.tidings.items;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 文本.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:23
 */
@ApiModel(description = "文本")
public class Text {
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    @ApiModelProperty(value = "内容", required = true)
    public String getContent() {
        return content;
    }

    @ApiModelProperty(value = "内容", required = true)
    public void setContent(String content) {
        this.content = content;
    }
}
