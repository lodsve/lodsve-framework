package message.wechat.validate;

import message.base.utils.EncryptUtils;
import message.base.utils.StringUtils;
import message.wechat.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 微信公众号平台配置服务器参数时，请求开发者服务器校验.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 13:23
 */
@Component
public class WeChatValidator {
    @Autowired
    private WeChatProperties properties;

    /**
     * 微信公众号平台配置服务器参数时，会请求开发者服务器，此方法返回{@link true}表明校验成功，可直接将接收到的<code>echostr</code>参数返回即可<br/>
     * 否则返回任意其他字符串
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return
     */
    public boolean validate(String signature, String timestamp, String nonce) {
        List<String> params = Arrays.asList(properties.getToken(), timestamp, nonce);
        Collections.sort(params);

        String encrypt = EncryptUtils.encodeSHA(StringUtils.join(params, StringUtils.EMPTY));
        if (StringUtils.equalsIgnoreCase(signature, encrypt)) {
            return true;
        } else {
            return false;
        }
    }
}
