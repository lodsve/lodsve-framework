/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.wechat.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lodsve.wechat.beans.AccessToken;
import lodsve.wechat.beans.JsApiTicket;
import lodsve.wechat.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 获取最重要参数[accessToken/jsapi_ticket]的两个方法.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/21 下午5:18
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
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e.getMessage());
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
