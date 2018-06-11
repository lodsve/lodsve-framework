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

package lodsve.wechat.api.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lodsve.core.utils.EncryptUtils;
import lodsve.core.utils.RandomUtils;
import lodsve.core.utils.StringUtils;
import lodsve.wechat.beans.JsApiConfig;
import lodsve.wechat.properties.WeChatProperties;
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/23 上午11:11
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
