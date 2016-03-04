package message.wechat.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import message.wechat.beans.CustomerServer;
import message.wechat.core.WeChat;
import message.wechat.core.WeChatRequest;
import message.wechat.core.WeChatUrl;
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
public class CustomerService {
    /**
     * 增加客服
     *
     * @param customerServer 客服
     */
    public void add(CustomerServer customerServer) {
        Assert.notNull(customerServer);
        WeChatRequest.post(String.format(WeChatUrl.ADD_CUSTOMER_SERVICE, WeChat.getAccessToken()), customerServer, new TypeReference<Void>() {
        });
    }

    /**
     * 修改客服
     *
     * @param customerServer 客服
     */
    public void update(CustomerServer customerServer) {
        Assert.notNull(customerServer);
        Assert.hasText(customerServer.getAccount());

        WeChatRequest.post(String.format(WeChatUrl.UPDATE_CUSTOMER_SERVICE, WeChat.getAccessToken()), customerServer, new TypeReference<Void>() {
        });
    }

    /**
     * 删除客服
     *
     * @param customerServer 客服
     */
    public void delete(CustomerServer customerServer) {
        Assert.notNull(customerServer);
        Assert.hasText(customerServer.getAccount());

        WeChatRequest.post(String.format(WeChatUrl.DELETE_CUSTOMER_SERVICE, WeChat.getAccessToken()), customerServer, new TypeReference<Void>() {
        });
    }

    /**
     * 获取所有客服
     */
    public List<CustomerServer> list() {
        Map<String, List<CustomerServer>> customerServers = WeChatRequest.get(String.format(WeChatUrl.LIST_CUSTOMER_SERVICE, WeChat.getAccessToken()),
                new TypeReference<Map<String, List<CustomerServer>>>() {
                });
        return MapUtils.isNotEmpty(customerServers) ? customerServers.get("kf_list") : Collections.<CustomerServer>emptyList();
    }
}
