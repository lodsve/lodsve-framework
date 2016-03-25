package message.amqp.core;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * 由于原来的amqp在接受json数据转成Java对象时，需要按照原来message中__TypeId__来转换，这里重写类是为了替换这里的__TypeId__为消费者的<br/>
 * 数据类型，并且加入泛型的判断.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-11-3 19:09
 */
public class LodsveMessageListenerAdapter extends MessageListenerAdapter {
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
                    throw new AmqpIllegalStateException(
                            "MessageListenerAdapter cannot handle a "
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
                Class<?> clazz = method.getParameterTypes()[0];
                String className = clazz.getName();
                setMessageHeader(message.getMessageProperties(),
                        AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME, className);

                // 泛型的参数类型
                Type[] types = method.getGenericParameterTypes();
                // 存在泛型
                if (types.length >= 0 && types[0] instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) types[0];
                    // 泛型
                    Type[] actualTypeArguments = pType.getActualTypeArguments();

                    if (Collection.class.isAssignableFrom(clazz) && actualTypeArguments.length == 1) {
                        // collection
                        setListMessageHeader(message.getMessageProperties(), actualTypeArguments[0]);
                    } else if (Map.class.isAssignableFrom(clazz) && actualTypeArguments.length == 2) {
                        // map
                        setMapMessageHeader(message.getMessageProperties(), actualTypeArguments);
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

    private void setListMessageHeader(MessageProperties properties, Type t) {
        if (!(t instanceof Class))
            return;

        setMessageHeader(properties, AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, ((Class<?>) t).getName());
    }

    private void setMapMessageHeader(MessageProperties properties, Type[] ts) {
        if (ts.length != 2)
            return;

        // key
        setMessageHeader(properties, AbstractJavaTypeMapper.DEFAULT_KEY_CLASSID_FIELD_NAME, ((Class<?>) ts[0]).getName());
        // content
        setMessageHeader(properties, AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, ((Class<?>) ts[1]).getName());
    }

    private void setMessageHeader(MessageProperties properties, String key, Object value) {
        properties.getHeaders().put(key, value);
    }
}
