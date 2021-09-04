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
