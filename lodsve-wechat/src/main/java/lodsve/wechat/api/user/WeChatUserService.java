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
package lodsve.wechat.api.user;

import com.fasterxml.jackson.core.type.TypeReference;
import lodsve.core.utils.StringUtils;
import lodsve.wechat.beans.User;
import lodsve.wechat.beans.UserQuery;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import lodsve.wechat.enums.Lang;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 微信用户操作.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/2 上午11:06
 */
@Component
public class WeChatUserService {
    /**
     * 设置备注名
     *
     * @param openId 用户标识
     * @param alias  新的备注名，长度必须小于30字符
     */
    public void updateUserAlias(String openId, String alias) {
        Assert.hasText(alias, "alias must be non-null!");
        Assert.isTrue(alias.length() <= 30, "alias length need lt or eq 30!");
        Assert.hasText(openId, "openId must be non-null!");

        Map<String, Object> params = new HashMap<>(2);
        params.put("openid", openId);
        params.put("remark", alias);

        WeChatRequest.post(String.format(WeChatUrl.UPDATE_USER_ALIAS, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 获取用户基本信息
     *
     * @param openId 普通用户的标识，对当前公众号唯一
     * @param lang   返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return 用户
     * @see Lang
     */
    public User getUser(String openId, Lang lang) {
        Assert.hasText(openId, "openId must be non-null!");
        if (null == lang) {
            lang = Lang.zh_CN;
        }

        return WeChatRequest.get(String.format(WeChatUrl.GET_USER_INFO, WeChat.accessToken(), openId, lang.toString()), new TypeReference<User>() {
        });
    }

    /**
     * 批量获取用户基本信息
     *
     * @param users 查询条件,只用到openId和language两个字段
     * @return 用户
     */
    public List<User> batchGetUser(List<UserQuery> users) {
        Assert.notNull(users, "users must be non-null!");

        List<Map<String, Object>> params = new ArrayList<>();
        for (UserQuery user : users) {
            Map<String, Object> p = new HashMap<>(2);

            p.put("openid", user.getOpenId());
            p.put("lang", user.getLanguage().toString());

            params.add(p);
        }

        return WeChatRequest.post(String.format(WeChatUrl.BATCH_GET_USER_INFO, WeChat.accessToken()), Collections.singletonMap("user_list", params), new TypeReference<Map<String, List<User>>>() {
        }).get("user_info_list");
    }

    /**
     * 获取关注者列表
     *
     * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
     * @return 关注者openId
     */
    @SuppressWarnings("unchecked")
    public List<String> listSubscribe(String nextOpenId) {
        String url = String.format(WeChatUrl.LIST_SUBSCRIBER, WeChat.accessToken(), StringUtils.isEmpty(nextOpenId) ? StringUtils.EMPTY : nextOpenId);
        Map<String, Object> result = WeChatRequest.get(url, new TypeReference<Map<String, Object>>() {
        });

        return (List<String>) ((Map) result.get("data")).get("openid");
    }
}
