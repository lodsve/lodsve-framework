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
package lodsve.wechat.api.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import lodsve.wechat.beans.Menu;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import lodsve.wechat.exception.WeChatException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 菜单操作.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午4:17
 */
@Component
public class WeChatMenuService {
    public static final int MENU_LENGTH = 3;

    /**
     * 创建菜单(会删除原来的菜单)
     *
     * @param menus 需要创建的菜单
     */
    public void create(Menu... menus) {
        if (ArrayUtils.isEmpty(menus)) {
            return;
        }

        if (menus.length > MENU_LENGTH) {
            throw new WeChatException(107001, "微信按钮个数限制小于等于3个");
        }
        for (Menu m : menus) {
            List<Menu> subButtons = m.getSubButtons();
            if (subButtons != null && subButtons.size() > 5) {
                throw new WeChatException(107002, "微信子按钮个数限制小于等于5个");
            }
        }

        WeChatRequest.post(String.format(WeChatUrl.CREATE_MENUS, WeChat.accessToken()), Collections.singletonMap("button", Arrays.asList(menus)), new TypeReference<Void>() {
        });
    }

    /**
     * 获取全部菜单
     *
     * @return 全部菜单
     */
    public List<Menu> getMenus() {
        try {
            Map<String, Map<String, List<Map<String, Object>>>> result = WeChatRequest.get(String.format(WeChatUrl.GET_MENUS, WeChat.accessToken()),
                    new TypeReference<Map<String, Map<String, List<Map<String, Object>>>>>() {
                    });
            List<Map<String, Object>> temp = result.get("menu").get("button");

            List<Menu> menus = new ArrayList<>(temp.size());
            for (Map<String, Object> t : temp) {
                Menu m = WeChatRequest.evalMap(t, new TypeReference<Menu>() {
                });
                menus.add(m);
            }

            return menus;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 删除全部菜单
     */
    public void delete() {
        WeChatRequest.get(String.format(WeChatUrl.DELETE_MENUS, WeChat.accessToken()), new TypeReference<Void>() {
        });
    }
}
