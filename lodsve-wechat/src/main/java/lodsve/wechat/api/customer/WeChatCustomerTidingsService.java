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
import lodsve.wechat.beans.tidings.Tidings;
import lodsve.wechat.core.WeChat;
import lodsve.wechat.core.WeChatRequest;
import lodsve.wechat.core.WeChatUrl;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 发送客服消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午12:25
 */
@Component
public class WeChatCustomerTidingsService {
    /**
     * 客服发送消息
     *
     * @param tidings 消息
     */
    public void send(Tidings tidings) {
        Assert.notNull(tidings, "tidings must be non-null!");

        WeChatRequest.post(String.format(WeChatUrl.CUSTOMER_SEND_TIDINGS, WeChat.accessToken()), tidings, new TypeReference<Void>() {
        });
    }
}
