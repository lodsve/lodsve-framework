package message.wechat.api.user;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import message.wechat.beans.Group;
import message.wechat.core.WeChat;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 微信组操作.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/2 上午11:06
 */
@Component
public class WeChatGroupService {

    /**
     * 创建分组
     *
     * @param groupName 分组名称
     */
    public Group create(String groupName) {
        Assert.hasText(groupName);
        Assert.isTrue(groupName.length() <= 30);

        Map<String, Group> map = WeChatRequest.post(String.format(WeChatUrl.ADD_GROUP, WeChat.accessToken()), null,
                new TypeReference<Map<String, Group>>() {
                });
        return MapUtils.isNotEmpty(map) ? map.get("group") : null;
    }

    /**
     * 查询所有分组
     *
     * @return 所有分组
     */
    public List<Group> list() {
        Map<String, List<Group>> map = WeChatRequest.post(String.format(WeChatUrl.LIST_GROUP, WeChat.accessToken()), null,
                new TypeReference<Map<String, List<Group>>>() {
                });

        return MapUtils.isNotEmpty(map) ? map.get("groups") : null;
    }

    /**
     * 查询用户所在分组
     *
     * @param openId 用户的OpenID
     * @return 用户所属的groupid
     */
    public int findUserGroup(String openId) {
        Assert.hasLength(openId);

        Map<String, Integer> group = WeChatRequest.post(String.format(WeChatUrl.FIND_USER_GROUP, WeChat.accessToken()), Collections.singletonMap("openid", openId), new TypeReference<Map<String, Integer>>() {
        });

        if (MapUtils.isEmpty(group)) {
            return -1;
        }

        return group.get("groupid");
    }

    /**
     * 修改分组名
     *
     * @param groupId   分组id，由微信分配
     * @param groupName 分组名字（30个字符以内）
     */
    public void updateGroup(int groupId, String groupName) {
        Assert.isTrue(groupId > 0);
        Assert.hasText(groupName);
        Assert.isTrue(groupName.length() <= 30);

        Map<String, Object> params = new HashMap<>(2);
        params.put("id", groupId);
        params.put("name", groupName);

        WeChatRequest.post(String.format(WeChatUrl.UPDATE_GROUP, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 移动用户分组
     *
     * @param openId    用户唯一标识符
     * @param toGroupId 分组id
     */
    public void moveUserGroup(String openId, int toGroupId) {
        Assert.isTrue(toGroupId > 0);
        Assert.hasText(openId);

        Map<String, Object> params = new HashMap<>(2);
        params.put("openid", openId);
        params.put("to_groupid", toGroupId);

        WeChatRequest.post(String.format(WeChatUrl.MOVE_USER_GROUP, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 批量移动用户分组
     *
     * @param openIds   用户唯一标识符
     * @param toGroupId 分组id
     */
    public void batchMoveUserGroup(List<String> openIds, int toGroupId) {
        Assert.isTrue(toGroupId > 0);
        Assert.isTrue(openIds != null && openIds.size() > 0);

        Map<String, Object> params = new HashMap<>(2);
        params.put("openid_list", openIds);
        params.put("to_groupid", toGroupId);

        WeChatRequest.post(String.format(WeChatUrl.BATCH_MOVE_USER_GROUP, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }

    /**
     * 批量移动用户分组
     *
     * @param groupId 分组的id
     */
    public void deleteGroup(int groupId) {
        Assert.isTrue(groupId > 0);

        Map<String, Map<String, Integer>> params = Collections.singletonMap("group", Collections.singletonMap("id", groupId));

        WeChatRequest.post(String.format(WeChatUrl.DELETE_GROUP, WeChat.accessToken()), params, new TypeReference<Void>() {
        });
    }
}
