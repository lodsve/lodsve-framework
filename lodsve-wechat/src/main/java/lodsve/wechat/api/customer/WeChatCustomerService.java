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

package lodsve.wechat.api.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.core.utils.EncryptUtils;
import lodsve.wechat.beans.CustomerServer;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 客服人员的操作.
 *
 * @author sunhao(sunhao.java@gmail.com)
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
        Assert.hasText(account);
        Assert.hasText(nickName);
        Assert.hasText(password);

        Map<String, Object> params = new HashMap<>(3);
        params.put("kf_account", account);
        params.put("nickname", nickName);
        params.put("password", EncryptUtils.encodeMD5(password));

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
        Assert.hasText(account);
        Assert.hasText(nickName);
        Assert.hasText(password);

        Map<String, Object> params = new HashMap<>(3);
        params.put("kf_account", account);
        params.put("nickname", nickName);
        params.put("password", EncryptUtils.encodeMD5(password));

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
        Assert.hasText(account);
        Assert.hasText(nickName);
        Assert.hasText(password);

        Map<String, Object> params = new HashMap<>(3);
        params.put("kf_account", account);
        params.put("nickname", nickName);
        params.put("password", EncryptUtils.encodeMD5(password));

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
        return MapUtils.isNotEmpty(customerServers) ? customerServers.get("kf_list") : Collections.<CustomerServer>emptyList();
    }
}
