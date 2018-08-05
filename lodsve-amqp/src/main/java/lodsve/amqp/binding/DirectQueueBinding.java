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

package lodsve.amqp.binding;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import java.util.Collections;

/**
 * direct queue.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-08-02 13:49
 */
public class DirectQueueBinding extends Binding {
    public DirectQueueBinding(DirectExchange exchange, Queue queue, String routingKey) {
        super(queue.getName(), DestinationType.QUEUE, exchange.getName(), routingKey, Collections.<String, Object>emptyMap());
    }

    public DirectQueueBinding(String exchange, String queue, String routingKey) {
        super(queue, DestinationType.QUEUE, exchange, routingKey, Collections.<String, Object>emptyMap());
    }
}
