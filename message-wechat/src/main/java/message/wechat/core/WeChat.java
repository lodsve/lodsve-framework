package message.wechat.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import message.base.utils.StringUtils;
import message.wechat.beans.AccessToken;
import message.wechat.beans.JsApiTicket;
import message.wechat.config.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:18
 */
@Component
public final class WeChat {
    private static Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(90, TimeUnit.MINUTES).build();
    private static WeChatProperties properties;

    @Autowired
    public WeChat(WeChatProperties properties) {
        WeChat.properties = properties;
    }

    public static String getAccessToken() {
        try {
            return CACHE.get("accessToken", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return requestAccessToken().getAccessToken();
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return StringUtils.EMPTY;
    }

    public static String getJsApiTicket() {
        try {
            return CACHE.get("jsapi_ticket", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return requestJsApiTicket().getTicket();
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return StringUtils.EMPTY;
    }

    private static AccessToken requestAccessToken() {
        return WeChatRequest.get(String.format(WeChatUrl.GET_ACCESS_TOKEN, properties.getAppId(), properties.getAppSecret()), AccessToken.class);
    }

    private static JsApiTicket requestJsApiTicket() {
        return WeChatRequest.get(String.format(WeChatUrl.GET_JSP_API_TICKET, getAccessToken()), JsApiTicket.class);
    }
}
