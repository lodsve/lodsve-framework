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

import lodsve.core.utils.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

import java.util.Collections;

/**
 * fanout queue.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-08-02 13:48
 */
public class FanoutQueueBinding extends Binding {
    public FanoutQueueBinding(FanoutExchange exchange, Queue queue) {
        super(queue.getName(), DestinationType.QUEUE, exchange.getName(), StringUtils.EMPTY, Collections.emptyMap());
    }

    public FanoutQueueBinding(String exchange, String queue) {
        super(queue, DestinationType.QUEUE, exchange, StringUtils.EMPTY, Collections.emptyMap());
    }
}
