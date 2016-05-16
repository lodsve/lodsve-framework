package lodsve.wechat.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lodsve.wechat.beans.AccessToken;
import lodsve.wechat.beans.JsApiTicket;
import lodsve.wechat.config.WeChatProperties;
import lodsve.wechat.exception.WeChatException;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取最重要参数[accessToken/jsapi_ticket]的两个方法.
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

    public static String accessToken() {
        try {
            return CACHE.get("accessToken", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return requestAccessToken().getAccessToken();
                }
            });
        } catch (ExecutionException e) {
            throw new WeChatException(HttpStatus.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public static String jsApiTicket() {
        try {
            return CACHE.get("jsapi_ticket", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return requestJsApiTicket().getTicket();
                }
            });
        } catch (ExecutionException e) {
            throw new WeChatException(HttpStatus.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private static AccessToken requestAccessToken() {
        return WeChatRequest.get(String.format(WeChatUrl.GET_ACCESS_TOKEN, properties.getAppId(), properties.getAppSecret()), new TypeReference<AccessToken>() {
        });
    }

    private static JsApiTicket requestJsApiTicket() {
        return WeChatRequest.get(String.format(WeChatUrl.GET_JSP_API_TICKET, accessToken()), new TypeReference<JsApiTicket>() {
        });
    }
}
