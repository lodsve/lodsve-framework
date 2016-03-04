package message.wechat.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import message.wechat.beans.Menu;
import message.wechat.core.WeChat;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 菜单操作.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午4:17
 */
@Component
public class MenuService {

    /**
     * 创建菜单(会删除原来的菜单)
     *
     * @param menus 需要创建的菜单
     */
    public void create(Menu... menus) {
        if (ArrayUtils.isEmpty(menus)) {
            return;
        }

        if (menus.length > 3) {
            throw new RuntimeException("微信按钮个数限制小于等于3个");
        }
        for (Menu m : menus) {
            List<Menu> subButtons = m.getSubButtons();
            if (subButtons != null && subButtons.size() > 5) {
                throw new RuntimeException("微信子按钮个数限制小于等于5个");
            }
        }

        WeChatRequest.post(String.format(WeChatUrl.CREATE_MENUS, WeChat.getAccessToken()), Collections.singletonMap("button", Arrays.asList(menus)), new TypeReference<Void>() {
        });
    }

    /**
     * 获取全部菜单
     *
     * @return 全部菜单
     */
    public List<Menu> getMenus() {
        try {
            Map<String, Map<String, List<Map<String, Object>>>> result = WeChatRequest.get(String.format(WeChatUrl.GET_MENUS, WeChat.getAccessToken()),
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
        WeChatRequest.get(String.format(WeChatUrl.DELETE_MENUS, WeChat.getAccessToken()), new TypeReference<Void>() {
        });
    }
}
