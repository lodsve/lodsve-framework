package message.wechat.beans.tidings;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import message.wechat.beans.tidings.items.Text;

/**
 * 文本客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:56
 */
@ApiModel(description = "文本客服消息")
public class TextTidings extends Tidings {
    public TextTidings() {
        setTidingsType(TidingsType.text);
    }

    @ApiModelProperty(value = "文本客服消息", required = true)
    private Text text;

    @ApiModelProperty(value = "文本客服消息", required = true)
    public Text getText() {
        return text;
    }

    @ApiModelProperty(value = "文本客服消息", required = true)
    public void setText(Text text) {
        this.text = text;
    }
}
