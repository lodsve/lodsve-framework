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

package lodsve.wechat.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lodsve.web.utils.RestUtils;
import lodsve.core.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * 微信请求的封装,包括处理异常.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/21 下午5:26
 */
public final class WeChatRequest {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRequest.class);
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

    @SuppressWarnings("unchecked")
    public static <T> T get(String url, TypeReference<T> typeReference, Object... params) {
        Assert.hasText(url);

        Map<String, Object> result;
        if (url.contains("oauth")) {
            String returnValue = RestUtils.get(url, String.class, params);

            try {
                result = OBJECT_MAPPER.readValue(returnValue, new TypeReference<Map>() {
                });
            } catch (IOException e) {
                return null;
            }
        } else {
            result = RestUtils.get(url, Map.class, params);
        }

        if (isError(result)) {
            throw new RuntimeException((String) result.get("errmsg"));
        }

        return evalMap(result, typeReference);
    }

    @SuppressWarnings("unchecked")
    public static <T> T post(String url, Object object, TypeReference<T> typeReference, Object... params) {
        Assert.hasText(url);

        Map<String, Object> result = RestUtils.post(url, object, Map.class, params);
        if (isError(result)) {
            throw new RuntimeException((String) result.get("errmsg"));
        }

        return evalMap(result, typeReference);
    }

    private static boolean isError(Map<String, Object> result) {
        if (MapUtils.isEmpty(result)) {
            return true;
        }

        Object errcode = result.get("errcode");
        String errcodeStr = errcode == null ? StringUtils.EMPTY : errcode.toString();

        return !("".equals(errcodeStr) || "0".equals(errcodeStr));

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
