package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Text;

/**
 * 文本客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:56
 */
public class TextTidings extends Tidings {
    public TextTidings() {
        setTidingsType(TidingsType.text);
    }

    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
