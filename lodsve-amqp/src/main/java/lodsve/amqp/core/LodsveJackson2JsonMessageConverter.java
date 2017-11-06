package lodsve.amqp.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.IOException;

/**
 * 修改Jackson反序列化时对泛型的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午1:28
 */
public class LodsveJackson2JsonMessageConverter extends Jackson2JsonMessageConverter {
    private static Log log = LogFactory.getLog(LodsveJackson2JsonMessageConverter.class);
    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.contains("json")) {
                String encoding = properties.getContentEncoding();
                if (encoding == null) {
                    encoding = getDefaultCharset();
                }
                try {
                    if (getClassMapper() == null) {
                        JavaType targetJavaType = getJavaTypeMapper().toJavaType(message.getMessageProperties());
                        content = convertBytesToObject(message.getBody(), encoding, targetJavaType);
                    } else {
                        Class<?> targetClass = getClassMapper().toClass(message.getMessageProperties());
                        content = convertBytesToObject(message.getBody(), encoding, targetClass);
                    }
                } catch (IOException e) {
                    throw new MessageConversionException("Failed to convert Message content", e);
                }
            } else {
                log.warn("Could not convert incoming message with content-type [" + contentType + "]");
            }
        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }

    private Object convertBytesToObject(byte[] body, String encoding, JavaType targetJavaType) throws IOException {
        String contentAsString = new String(body, encoding);
        return jsonObjectMapper.readValue(contentAsString, targetJavaType);
    }

    private Object convertBytesToObject(byte[] body, String encoding, Class<?> targetClass) throws IOException {
        String contentAsString = new String(body, encoding);
        return jsonObjectMapper.readValue(contentAsString, jsonObjectMapper.constructType(targetClass));
    }

    @Override
    public Jackson2JavaTypeMapper getJavaTypeMapper() {
        return new LodsveJackson2JavaTypeMapper();
    }
}
