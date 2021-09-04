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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 14:36
 */
public class JacksonConverter extends AbstractJsonConverter {
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
