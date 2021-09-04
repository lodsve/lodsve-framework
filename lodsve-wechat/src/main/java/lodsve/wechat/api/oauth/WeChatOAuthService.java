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
package lodsve.wechat.api.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import lodsve.core.utils.StringUtils;
import lodsve.core.utils.URLUtils;
import lodsve.wechat.beans.OAuthToken;
import lodsve.wechat.config.WeChatProperties;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import lodsve.wechat.exception.WeChatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * oauth认证.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午12:38
 */
@Component
public class WeChatOAuthService {
    @Autowired
    private WeChatProperties properties;

    /**
     * 获取OAuth认证的地址.
     *
     * @param getOpenIdUrl 获取openId的url
     * @param url          前台url
     * @param scope        用户授权的作用域，"snsapi_base" or "snsapi_userinfo"
     * @return 认证的地址
     */
    public String getOAuthUrl(String getOpenIdUrl, String url, String scope) {
        String weChatUrl = WeChatUrl.WEIXIN_AUTHORIZE_URL;
        String bizUrl;
        try {
            String systemUrl = getOpenIdUrl + "?url=";
            bizUrl = URLUtils.encodeURL(systemUrl + URLUtils.encodeURL(url, Charsets.UTF_8.displayName()), Charsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            bizUrl = StringUtils.EMPTY;
        }

        if (StringUtils.isEmpty(scope) || !Arrays.asList("snsapi_base", "snsapi_userinfo").contains(scope)) {
            scope = "snsapi_userinfo";
        }

        return String.format(weChatUrl, properties.getAppId(), bizUrl, scope);
    }

    /**
     * 通过code换取网页授权access_token
     *
     * @param code 微信OAuth认证第一步返回的code,用户同意授权，获取的code
     * @return
     * @see #getOAuthUrl(String, String, String)
     */
    public OAuthToken getOAuthToken(String code) {
        if (StringUtils.isBlank(code)) {
            throw new WeChatException(107003, "用户拒绝认证!");
        }

        OAuthToken oAuthToken = WeChatRequest.get(String.format(WeChatUrl.WEIXIN_TOKEN_URL, properties.getAppId(), properties.getAppSecret(), code), new TypeReference<OAuthToken>() {
        });
        checkOAuthToken(oAuthToken);

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
            throw new WeChatException(107005, "获取token失败!");
        }

        OAuthToken oAuthToken = WeChatRequest.get(String.format(WeChatUrl.WEIXIN_REFRESH_TOKEN_URL, properties.getAppId(), refreshToken), new TypeReference<OAuthToken>() {
        });
        checkOAuthToken(oAuthToken);

        return oAuthToken;
    }

    private void checkOAuthToken(OAuthToken oAuthToken) {
        if (oAuthToken == null) {
            throw new WeChatException(107004, "微信认证失败!");
        }

        if (StringUtils.isEmpty(oAuthToken.getAccessToken())) {
            throw new WeChatException(107004, "微信认证失败!");
        }
    }
}
