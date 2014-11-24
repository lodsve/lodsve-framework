package message.amqp.converter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-10-7 11:08
 */
public class FastJsonMessageConverter extends AbstractMessageConverter {
    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }
}
