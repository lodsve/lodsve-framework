package message.amqp.core;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 由于原来的amqp在接受json数据转成Java对象时，需要按照原来message中__TypeId__来转换，这里重写类是为了替换这里的__TypeId__为消费者的<br/>
 * 数据类型，并且加入泛型的判断.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-11-3 19:09
 */
public class MessageListenerAdapter4Jackson extends MessageListenerAdapter {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // Check whether the delegate is a MessageListener impl itself.
        // In that case, the adapter will simply act as a pass-through.
        Object delegate = getDelegate();
        if (delegate != this) {
            if (delegate instanceof ChannelAwareMessageListener) {
                if (channel != null) {
                    ((ChannelAwareMessageListener) delegate).onMessage(message, channel);
                    return;
                } else if (!(delegate instanceof MessageListener)) {
                    throw new AmqpIllegalStateException("MessageListenerAdapter cannot handle a "
                            + "ChannelAwareMessageListener delegate if it hasn't been invoked with a Channel itself");
                }
            }
            if (delegate instanceof MessageListener) {
                ((MessageListener) delegate).onMessage(message);
                return;
            }
        }

        String methodName = getListenerMethodName(message, null);
        if (methodName == null) {
            throw new AmqpIllegalStateException("No default listener method specified: "
                    + "Either specify a non-null value for the 'defaultListenerMethod' property or "
                    + "override the 'getListenerMethodName' method.");
        }

        Method[] methods = delegate.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                String className = method.getParameterTypes()[0].getName();
                message.getMessageProperties().getHeaders().put(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME, className);

                //泛型的参数类型(如果只有一个参数，那么就取第一个)
                Type[] types = method.getGenericParameterTypes();
                if (types.length >= 0 && types[0] instanceof ParameterizedType) {
                    //存在泛型
                    ParameterizedType pType = (ParameterizedType) types[0];
                    Type t = pType.getActualTypeArguments()[0];
                    if (t instanceof Class) {
                        //泛型类型
                        message.getMessageProperties().getHeaders().put(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, ((Class) t).getName());
                    }
                }

                Object convertedMessage = extractMessage(message);
                Object result = method.invoke(delegate, convertedMessage);
                if (result != null) {
                    handleResult(result, message, channel);
                }
                return;
            }
        }
    }
}
