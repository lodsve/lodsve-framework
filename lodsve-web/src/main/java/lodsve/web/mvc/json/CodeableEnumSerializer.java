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
package lodsve.web.mvc.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lodsve.core.bean.Codeable;
import lodsve.web.mvc.config.WebMvcConfiguration;

import java.io.IOException;

/**
 * Jackson序列化枚举时，将枚举变成{value: '', name: ''}.<br/>
 * {@link WebMvcConfiguration#objectMapper()}
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/6/24 下午9:31
 */
public class CodeableEnumSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(Enum value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (!(value instanceof Codeable)) {
            jsonGenerator.writeNumber(value.ordinal());
            return;
        }

        Codeable codeable = (Codeable) value;

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", codeable.getCode());
        jsonGenerator.writeStringField("title", codeable.getTitle());
        jsonGenerator.writeEndObject();
    }
}
