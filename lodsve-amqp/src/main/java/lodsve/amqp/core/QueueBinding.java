package lodsve.amqp.core;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

import java.util.Collections;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/7 11:18
 */
public class QueueBinding extends Binding {
    public QueueBinding(FanoutExchange exchange, Queue queue) {
        super(queue.getName(), DestinationType.QUEUE, exchange.getName(), null, Collections.<String, Object>emptyMap());
    }

    public QueueBinding(Exchange exchange, Queue queue, String routingKey) {
        super(queue.getName(), DestinationType.QUEUE, exchange.getName(), routingKey, Collections.<String, Object>emptyMap());
    }

    public QueueBinding(String exchange, String queue, String routingKey) {
        super(queue, DestinationType.QUEUE, exchange, routingKey, Collections.<String, Object>emptyMap());
    }
}
