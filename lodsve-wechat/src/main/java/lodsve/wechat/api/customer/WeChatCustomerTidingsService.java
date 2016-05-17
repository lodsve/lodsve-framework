package lodsve.wechat.api.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import lodsve.wechat.beans.tidings.Tidings;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 发送客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:25
 */
@Component
public class WeChatCustomerTidingsService {
    /**
     * 客服发送消息
     *
     * @param tidings 消息
     */
    public void send(Tidings tidings) {
        Assert.notNull(tidings);

        WeChatRequest.post(String.format(WeChatUrl.CUSTOMER_SEND_TIDINGS, WeChat.accessToken()), tidings, new TypeReference<Void>() {
        });
    }
}
