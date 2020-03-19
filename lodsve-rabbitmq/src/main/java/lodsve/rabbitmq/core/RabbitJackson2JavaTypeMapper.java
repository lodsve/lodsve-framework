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

package lodsve.rabbitmq.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.core.utils.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * 修改Jackson反序列化时对泛型的处理.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/18 下午1:38
 */
public class RabbitJackson2JavaTypeMapper extends DefaultJackson2JavaTypeMapper {
    private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();

    @Override
    public JavaType toJavaType(MessageProperties properties) {
        String classTypeId = retrieveHeader(properties, getClassIdFieldName());
        String contentTypeId = retrieveHeader(properties, getContentClassIdFieldName());
        String keyTypeId = retrieveHeader(properties, getKeyClassIdFieldName());

        if (StringUtils.isEmpty(contentTypeId)) {
            return JSON_OBJECT_MAPPER.getTypeFactory().constructType(forName(classTypeId));
        }

        if (StringUtils.isEmpty(keyTypeId)) {
            return JSON_OBJECT_MAPPER.getTypeFactory().constructParametricType(forName(classTypeId), forName(contentTypeId));
        } else {
            return JSON_OBJECT_MAPPER.getTypeFactory().constructParametricType(forName(classTypeId), forName(keyTypeId), forName(contentTypeId));
        }
    }

    @Override
    public void fromJavaType(JavaType javaType, MessageProperties properties) {
        addHeader(properties, getClassIdFieldName(), javaType.getRawClass());

        if (javaType.isContainerType() && !javaType.isArrayType()) {
            addHeader(properties, getContentClassIdFieldName(), javaType.getContentType().getRawClass());
        }

        if (javaType.getKeyType() != null) {
            addHeader(properties, getKeyClassIdFieldName(), javaType.getKeyType().getRawClass());
        }
    }

    private Class<?> forName(String className) {
        try {
            return ClassUtils.forName(className, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Object.class;
    }

    @Override
    protected String retrieveHeader(MessageProperties properties, String headerName) {
        Map<String, Object> headers = properties.getHeaders();
        Object classIdFieldNameValue = headers.get(headerName);
        String classId = null;
        if (classIdFieldNameValue != null) {
            classId = classIdFieldNameValue.toString();
        }

        return classId;
    }
}
