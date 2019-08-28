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

package lodsve.rocketmq.configuration;

import io.netty.util.internal.ConcurrentSet;
import lodsve.rocketmq.core.RocketMQTemplate;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.DisposableBean;

import java.util.Set;

public class TransactionHandlerRegistry implements DisposableBean {
    private final Set<String> listenerContainers = new ConcurrentSet<>();
    private RocketMQTemplate rocketMQTemplate;

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
