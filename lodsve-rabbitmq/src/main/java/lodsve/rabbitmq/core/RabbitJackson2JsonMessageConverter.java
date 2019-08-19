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

package lodsve.rabbitmq.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 修改Jackson反序列化时对泛型的处理.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/18 下午1:28
 */
public class RabbitJackson2JsonMessageConverter extends Jackson2JsonMessageConverter {
    private final static Log log = LogFactory.getLog(RabbitJackson2JsonMessageConverter.class);
    private final static String CONTENT_TYPE_JSON = "json";
    private final ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.contains(CONTENT_TYPE_JSON)) {
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

    @Override
    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes;
        try {
            String jsonString = this.jsonObjectMapper.writeValueAsString(objectToConvert);
            bytes = jsonString.getBytes(getDefaultCharset());
        } catch (IOException e) {
            throw new MessageConversionException("Failed to convert Message content", e);
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(getDefaultCharset());
        messageProperties.setContentLength(bytes.length);

        if (getClassMapper() == null) {
            Class<?> target = objectToConvert.getClass();
            JavaType javaType = this.jsonObjectMapper.constructType(target);
            if (Collection.class.isAssignableFrom(target)) {
                // collection
                Collection c = (Collection) objectToConvert;
                if (CollectionUtils.isNotEmpty(c)) {
                    Object obj = CollectionUtils.get(c, 0);
                    javaType = jsonObjectMapper.getTypeFactory().constructParametricType(target, obj.getClass());
                }
            } else if (Map.class.isAssignableFrom(target)) {
                // map
                Map m = (Map) objectToConvert;
                if (MapUtils.isNotEmpty(m)) {
                    Map.Entry entry = (Map.Entry) CollectionUtils.get(m, 0);
                    Class<?> keyClass = entry.getKey().getClass();
                    Class<?> valueClass = entry.getValue().getClass();

                    javaType = jsonObjectMapper.getTypeFactory().constructParametricType(target, keyClass, valueClass);
                }
            }

            getJavaTypeMapper().fromJavaType(javaType, messageProperties);
        } else {
            getClassMapper().fromClass(objectToConvert.getClass(), messageProperties);
        }

        return new Message(bytes, messageProperties);
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
        return new RabbitJackson2JavaTypeMapper();
    }
}
