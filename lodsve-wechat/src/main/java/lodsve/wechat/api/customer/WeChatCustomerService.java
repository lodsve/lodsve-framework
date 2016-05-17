package lodsve.wechat.api.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.base.utils.EncryptUtils;
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
