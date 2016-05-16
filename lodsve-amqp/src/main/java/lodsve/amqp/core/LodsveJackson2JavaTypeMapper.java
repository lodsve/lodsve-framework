package lodsve.amqp.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.base.utils.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * 修改Jackson反序列化时对泛型的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午1:38
 */
public class LodsveJackson2JavaTypeMapper extends DefaultJackson2JavaTypeMapper {
    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Override
    public JavaType toJavaType(MessageProperties properties) {
        String classTypeId = retrieveHeader(properties, getClassIdFieldName());
        String contentTypeId = retrieveHeader(properties, getContentClassIdFieldName());
        String keyTypeId = retrieveHeader(properties, getKeyClassIdFieldName());

        if(StringUtils.isEmpty(contentTypeId)) {
            return jsonObjectMapper.getTypeFactory().constructType(forName(classTypeId));
        }

        if (StringUtils.isEmpty(keyTypeId)) {
            return jsonObjectMapper.getTypeFactory().constructParametricType(forName(classTypeId), forName(contentTypeId));
        } else {
            return jsonObjectMapper.getTypeFactory().constructParametricType(forName(classTypeId), forName(keyTypeId), forName(contentTypeId));
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
