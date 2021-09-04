/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.wechat.core;

import com.fasterxml.jackson.core.type.TypeReference;
import lodsve.wechat.beans.AccessToken;
import lodsve.wechat.beans.JsApiTicket;
import lodsve.wechat.config.WeChatProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 获取最重要参数[accessToken/jsapi_ticket]的两个方法.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/21 下午5:18
 */
@Component
public final class WeChat {
    private static WeChatProperties properties;
    private static WeChatCache weChatCache;

    @Autowired
    public WeChat(ObjectProvider<WeChatProperties> properties, ObjectProvider<WeChatCache> weChatCacheObjectProvider) {
        WeChat.properties = properties.getIfAvailable();
        WeChat.weChatCache = weChatCacheObjectProvider.getIfAvailable(WeChatCache::new);
    }

    public static String accessToken() {
        try {
            return weChatCache.get("accessToken", () -> requestAccessToken().getAccessToken());
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String jsApiTicket() {
        try {
            return weChatCache.get("jsapi_ticket", () -> requestJsApiTicket().getTicket());
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
