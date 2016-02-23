package message.wechat.base;

import java.util.List;
import java.util.Map;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
import message.wechat.core.WeChat;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 上午11:11
 */
@Component
public class WeChatService {
    public List<String> getIps() {
        String accessToken = WeChat.getAccessToken();
        Map<String, List<String>> ips = WeChatRequest.get(WeChatUrl.GET_IPS, Map.class, accessToken);
        return ips.get("ip_list");
    }
}
