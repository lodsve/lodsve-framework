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
package lodsve.rocketmq.configuration;

import io.netty.util.internal.ConcurrentSet;
import lodsve.rocketmq.core.RocketMQTemplate;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.DisposableBean;

import java.util.Set;

public class TransactionHandlerRegistry implements DisposableBean {
    private final Set<String> listenerContainers = new ConcurrentSet<>();
    private final RocketMQTemplate rocketMQTemplate;

    public TransactionHandlerRegistry(RocketMQTemplate template) {
        this.rocketMQTemplate = template;
    }

    @Override
    public void destroy() throws Exception {
        listenerContainers.clear();
    }

    public void registerTransactionHandler(TransactionHandler handler) throws MQClientException {
        if (listenerContainers.contains(handler.getName())) {
            throw new MQClientException(-1,
                String
                    .format("The transaction name [%s] has been defined in TransactionListener [%s]", handler.getName(),
                        handler.getBeanName()));
        }
        listenerContainers.add(handler.getName());

        rocketMQTemplate.createAndStartTransactionMQProducer(handler.getName(), handler.getListener(), handler.getCheckExecutor());
    }
}
