/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.wechat.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lodsve.core.utils.StringUtils;
import lodsve.web.utils.RestUtils;
import lodsve.wechat.exception.WeChatException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * 微信请求的封装,包括处理异常.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/21 下午5:26
 */
public final class WeChatRequest {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRequest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Boolean.class, new JsonDeserializer<Boolean>() {
            @Override
            public Boolean deserialize(JsonParser jp, DeserializationContext context) throws IOException {
                JsonToken currentToken = jp.getCurrentToken();

                if (currentToken.equals(JsonToken.VALUE_STRING)) {
                    String text = jp.getText().trim();

                    if ("yes".equalsIgnoreCase(text) || "1".equals(text)) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
                    return getNullValue(context);
                }

                throw new WeChatException("Can not parse!");
            }

            @Override
            public Boolean getNullValue(DeserializationContext context) throws JsonMappingException {
                return Boolean.FALSE;
            }
        });
        OBJECT_MAPPER.registerModule(module);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String url, TypeReference<T> typeReference, Object... params) {
        Assert.hasText(url, "url must be non-null!");

        Map<String, Object> result;
        if (url.contains("oauth")) {
            String returnValue = RestUtils.get(url, String.class, params);

            try {
                result = OBJECT_MAPPER.readValue(returnValue, new TypeReference<Map<String, Object>>() {
                });
            } catch (IOException e) {
                throw new WeChatException(e.getMessage());
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
        Assert.hasText(url, "url must be non-null!");

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
            throw new WeChatException(e.getMessage());
        }
    }
}
