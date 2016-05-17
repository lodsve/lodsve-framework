package lodsve.wechat.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.Map;
import lodsve.base.utils.StringUtils;
import lodsve.wechat.exception.WeChatException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 微信请求的封装,包括处理异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:26
 */
public final class WeChatRequest {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRequest.class);
    private static final RestTemplate template = new RestTemplate();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Boolean.class, new JsonDeserializer<Boolean>() {
            @Override
            public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                JsonToken currentToken = jp.getCurrentToken();

                if (currentToken.equals(JsonToken.VALUE_STRING)) {
                    String text = jp.getText().trim();

                    if ("yes".equalsIgnoreCase(text) || "1".equals(text)) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
                    return getNullValue();
                }

                throw ctxt.mappingException(Boolean.class);
            }

            @Override
            public Boolean getNullValue() {
                return Boolean.FALSE;
            }
        });
        OBJECT_MAPPER.registerModule(module);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T get(String url, TypeReference<T> typeReference, Object... params) {
        Assert.hasText(url);

        Map<String, Object> result;
        if (url.contains("oauth")) {
            String returnValue = template.getForObject(url, String.class, params);

            try {
                result = OBJECT_MAPPER.readValue(returnValue, new TypeReference<Map>() {
                });
            } catch (IOException e) {
                return null;
            }
        } else {
            result = template.getForObject(url, Map.class, params);
        }

        if (isError(result)) {
            throw new WeChatException((Integer) result.get("errcode"), (String) result.get("errmsg"));
        }

        return evalMap(result, typeReference);
    }

    public static <T> T post(String url, Object object, TypeReference<T> typeReference, Object... params) {
        Assert.hasText(url);

        Map<String, Object> result = template.postForObject(url, object, Map.class, params);
        if (isError(result)) {
            throw new WeChatException((Integer) result.get("errcode"), (String) result.get("errmsg"));
        }

        return evalMap(result, typeReference);
    }

    private static boolean isError(Map<String, Object> result) {
        if (MapUtils.isEmpty(result)) {
            return true;
        }

        Object errcode = result.get("errcode");
        String errcode_ = errcode == null ? StringUtils.EMPTY : errcode.toString();

        return !("".equals(errcode_) || "0".equals(errcode_));

    }

    public static <T> T evalMap(Map<String, Object> result, TypeReference<T> typeReference) {
        if (Void.class.equals(typeReference.getType())) {
            return null;
        }

        try {
            String json = OBJECT_MAPPER.writeValueAsString(result);

            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
