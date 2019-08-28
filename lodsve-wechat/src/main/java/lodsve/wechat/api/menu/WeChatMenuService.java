/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
