package message.amqp.configs;

import message.amqp.configs.parser.QueueDefParser;
import message.amqp.configs.parser.RabbitMqParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * namespace handler.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-9-30 15:59
 */
public class RabbitMqNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("rabbitMq", new RabbitMqParser());
        registerBeanDefinitionParser("queue-def", new QueueDefParser());
    }
}
