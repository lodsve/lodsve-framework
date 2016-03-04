package message.wechat.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import java.io.UnsupportedEncodingException;
import message.base.utils.StringUtils;
import message.base.utils.URLUtils;
import message.properties.ApplicationProperties;
import message.wechat.config.WeChatProperties;
import message.wechat.beans.OAuthToken;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * oauth认证.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午12:38
 */
@Component
public class OAuthService {
    @Autowired
    private WeChatProperties properties;
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * 获取OAuth认证的地址.
     *
     * @param url 前台url
     * @return
     */
    public String getOAuthUrl(String url) {
        String weChatUrl = WeChatUrl.WEIXIN_AUTHORIZE_URL;
        String bizUrl;
        try {
            String systemUrl = applicationProperties.getServerUrl() + "/wx/oauth?url=";
            bizUrl = URLUtils.encodeURL(systemUrl + URLUtils.encodeURL(url, Charsets.UTF_8.displayName()), Charsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            bizUrl = StringUtils.EMPTY;
        }

        return String.format(weChatUrl, properties.getAppId(), bizUrl);
    }

    /**
     * 获得包含用户openId的OAuthToken
     *
     * @param code 微信OAuth认证返回的code
     * @return
     */
    public OAuthToken getOAuthToken(String code) {
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("用户拒绝认证!");
        }

        OAuthToken oAuthToken = WeChatRequest.get(String.format(WeChatUrl.WEIXIN_TOKEN_URL, properties.getAppId(), properties.getAppSecret(), code), new TypeReference<OAuthToken>() {
        });
        if (oAuthToken == null) {
            throw new RuntimeException("微信认证失败!");
        }

        if (StringUtils.isEmpty(oAuthToken.getAccessToken())) {
            throw new RuntimeException("微信认证失败!");
        }

        return oAuthToken;
    }

    /**
     * 刷新包含用户openId的OAuthToken
     *
     * @param refreshToken 微信OAuth刷新的token
     * @return
     */
    public OAuthToken refreshOAuthToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new RuntimeException("获取token失败!");
        }

        OAuthToken oAuthToken = WeChatRequest.get(String.format(WeChatUrl.WEIXIN_REFRESH_TOKEN_URL, properties.getAppId(), refreshToken), new TypeReference<OAuthToken>() {
        });
        if (oAuthToken == null) {
            throw new RuntimeException("微信认证失败!");
        }

        if (StringUtils.isEmpty(oAuthToken.getAccessToken())) {
            throw new RuntimeException("微信认证失败!");
        }

        return oAuthToken;
    }
}
