package message.wechat.base;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import java.util.Arrays;
import message.base.utils.EncryptUtils;
import message.base.utils.RandomUtils;
import message.base.utils.StringUtils;
import message.wechat.beans.JsApiConfig;
import message.wechat.core.WeChat;
import message.wechat.config.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:16
 */
@Component
public class WeChatCommonsService {
    private static final String SIGN_STR = "jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s";

    @Autowired
    private WeChatProperties properties;

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        Assert.hasText(timestamp);
        Assert.hasText(nonce);
        Assert.hasText(signature);

        String[] params = new String[]{properties.getToken(), timestamp, nonce};
        Arrays.sort(params);
        if (signature.equalsIgnoreCase(Hashing.sha1().hashString(StringUtils.join(params), Charsets.UTF_8).toString())) {
            return true;
        } else {
            return false;
        }
    }

    public JsApiConfig jsApiConfig(String url) {
        JsApiConfig jac = new JsApiConfig();
        jac.setAppId(properties.getAppId());
        jac.setTimestamp(System.currentTimeMillis() / 1000);
        jac.setNonceStr(RandomUtils.randomString(16));

        String jsApiTicket = WeChat.getJsApiTicket();
        String signature = EncryptUtils.encodeSHA(String.format(SIGN_STR, jsApiTicket, jac.getNonceStr(), jac.getTimestamp(), url));
        jac.setSignature(signature);

        return jac;
    }
}
