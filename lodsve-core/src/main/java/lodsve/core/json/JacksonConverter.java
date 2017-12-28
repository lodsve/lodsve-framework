package lodsve.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * jackson JsonUtils impl.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 14:36
 */
public class JacksonConverter implements JsonConverter {
    private final static Logger logger = LoggerFactory.getLogger(JacksonConverter.class);
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String toJson(Object obj) {
        Assert.notNull(obj);

        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            return StringUtils.EMPTY;
        }
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) {
        Assert.hasText(json);
        Assert.notNull(clazz);

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            return null;
        }
    }

    @Override
    public Map<String, Object> toMap(String json) {
        Assert.hasText(json);

        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            return Collections.emptyMap();
        }
    }
}
