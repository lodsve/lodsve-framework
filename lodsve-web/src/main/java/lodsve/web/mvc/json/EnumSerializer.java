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

package lodsve.web.mvc.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import lodsve.core.bean.Codeable;

import java.io.IOException;

/**
 * Jackson序列化枚举时，将枚举变成{value: '', name: ''}.<br/>
 * {@link lodsve.web.mvc.config.LodsveWebMvcConfigurerAdapter#customObjectMapper(ObjectMapper)}
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/6/24 下午9:31
 */
public class EnumSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (!(value instanceof Codeable)) {
            return;
        }

        Codeable codeable = (Codeable) value;

        jgen.writeStartObject();
        jgen.writeStringField("code", codeable.getCode());
        jgen.writeStringField("title", codeable.getTitle());
        jgen.writeEndObject();
    }
}
