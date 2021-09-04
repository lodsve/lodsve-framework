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
package lodsve.wechat.api.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lodsve.core.utils.EncryptUtils;
import lodsve.core.utils.RandomUtils;
import lodsve.core.utils.StringUtils;
import lodsve.wechat.beans.JsApiConfig;
import lodsve.wechat.config.WeChatProperties;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 微信service.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 上午11:11
 */
@Component
public class WeChatService {
    private static final String SIGN_STR = "jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s";

    @Autowired
    private WeChatProperties properties;

    public List<String> getIps() {
        String accessToken = WeChat.accessToken();
        Map<String, List<String>> ips = WeChatRequest.get(WeChatUrl.GET_IPS, new TypeReference<Map<String, List<String>>>() {
        }, accessToken);
        return ips.get("ip_list");
    }

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        Assert.hasText(timestamp);
        Assert.hasText(nonce);
        Assert.hasText(signature);

        String[] params = new String[]{properties.getToken(), timestamp, nonce};
        Arrays.sort(params);

        return signature.equalsIgnoreCase(Hashing.sha1().hashString(StringUtils.join(params), Charsets.UTF_8).toString());
    }

    public JsApiConfig jsApiConfig(String url) {
        JsApiConfig jac = new JsApiConfig();
        jac.setAppId(properties.getAppId());
        jac.setTimestamp(System.currentTimeMillis() / 1000);
        jac.setNonceStr(RandomUtils.randomString(16));

        String jsApiTicket = WeChat.jsApiTicket();
        String signature = EncryptUtils.encodeSHA(String.format(SIGN_STR, jsApiTicket, jac.getNonceStr(), jac.getTimestamp(), url));
        jac.setSignature(signature);

        return jac;
    }
}
