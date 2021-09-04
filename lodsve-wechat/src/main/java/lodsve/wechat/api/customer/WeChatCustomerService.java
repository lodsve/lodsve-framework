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
package lodsve.wechat.api.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import lodsve.core.utils.EncryptUtils;
import lodsve.wechat.beans.CustomerServer;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 客服人员的操作.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 上午10:04
 */
@Component
public class WeChatCustomerService {
    /**
     * 增加客服
     *
     * @param account  账号
     * @param nickName 昵称
     * @param password 登录密码
     */
    public void add(String account, String nickName, String password) {
        Map<String, Object> params = checkParamsAndReturn(account, nickName, password);

        WeChatRequest.post(String.format(WeChatUrl.ADD_CUSTOMER_SERVICE, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 修改客服
     *
     * @param account  账号
     * @param nickName 昵称
     * @param password 登录密码
     */
    public void update(String account, String nickName, String password) {
        Map<String, Object> params = checkParamsAndReturn(account, nickName, password);

        WeChatRequest.post(String.format(WeChatUrl.UPDATE_CUSTOMER_SERVICE, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 删除客服
     *
     * @param account  账号
     * @param nickName 昵称
     * @param password 登录密码
     */
    public void delete(String account, String nickName, String password) {
        Map<String, Object> params = checkParamsAndReturn(account, nickName, password);

        WeChatRequest.post(String.format(WeChatUrl.DELETE_CUSTOMER_SERVICE, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 获取所有客服
     */
    public List<CustomerServer> list() {
        Map<String, List<CustomerServer>> customerServers = WeChatRequest.get(String.format(WeChatUrl.LIST_CUSTOMER_SERVICE, WeChat.accessToken()),
                new TypeReference<Map<String, List<CustomerServer>>>() {
                });
        return MapUtils.isNotEmpty(customerServers) ? customerServers.get("kf_list") : Collections.emptyList();
    }

    private Map<String, Object> checkParamsAndReturn(String account, String nickName, String password) {
        Assert.hasText(account, "account must be non-null!");
        Assert.hasText(nickName, "nickName must be non-null!");
        Assert.hasText(password, "password must be non-null!");

        Map<String, Object> params = Maps.newHashMap();
        params.put("kf_account", account);
        params.put("nickname", nickName);
        params.put("password", EncryptUtils.encodeMD5(password));

        return params;
    }
}
