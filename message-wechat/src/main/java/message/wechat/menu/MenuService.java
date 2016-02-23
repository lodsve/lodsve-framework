package message.wechat.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import message.wechat.beans.Menu;
import message.wechat.core.WeChat;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午4:17
 */
@Component
public class MenuService {

    public void create(Menu... menus) {
        if (ArrayUtils.isEmpty(menus)) {
            return;
        }

        // 先删除所有
        delete();

        if (menus.length > 3) {
            throw new RuntimeException("微信按钮个数限制小于等于3个");
        }
        for (Menu m : menus) {
            List<Menu> subButtons = m.getSubButtons();
            if (subButtons != null && subButtons.size() > 5) {
                throw new RuntimeException("微信子按钮个数限制小于等于5个");
            }
        }

        WeChatRequest.post(String.format(WeChatUrl.CREATE_MENUS, WeChat.getAccessToken()), Arrays.asList(menus), Void.class);
    }

    public List<Menu> getMenus() {
        Map<String, Map<String, List<Menu>>> result = WeChatRequest.get(String.format(WeChatUrl.GET_MENUS, WeChat.getAccessToken()), Map.class);
        return result.get("menu").get("button");
    }

    public void delete() {
        WeChatRequest.get(String.format(WeChatUrl.DELETE_MENUS, WeChat.getAccessToken()), Void.class);
    }
}
