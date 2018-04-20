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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lodsve.core.bean.Codeable;
import lodsve.core.utils.StringUtils;

import java.io.IOException;

/**
 * Jackson反序列化枚举时，将code或者枚举value变成枚举.<br/>
 * {@link lodsve.web.mvc.json.CustomObjectMapper#CustomObjectMapper()}
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 2016/11/3 下午2:56
 */
public class EnumDeserializer extends JsonDeserializer<Enum> {
    @Override
    @SuppressWarnings("unchecked")
    public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Class<? extends Enum> clazz = getType(p);
        if (clazz == null) {
            return null;
        }

        Enum<?> result;
        try {
            result = Enum.valueOf(clazz, value);
        } catch (Exception e) {
            result = null;
        }

        if (result != null) {
            return result;
        }

        for (Enum<?> em : clazz.getEnumConstants()) {
            Codeable codeable = (Codeable) em;

            if (codeable.getCode().equals(value)) {
                result = em;
                break;
            }
        }

        return result;
    }

    @Override
    public Class<?> handledType() {
        return Enum.class;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> Class<T> getType(JsonParser p) throws IOException {
        Object object = p.getCurrentValue();
        Class<?> clazz = object.getClass();
        String fieldName = p.getCurrentName();

        Class<?> type;
        try {
            type = clazz.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            type = null;
        }

        if (support(type)) {
            return (Class<T>) type;
        }

        return null;
    }

    private boolean support(Class<?> clazz) {
        return clazz != null && Enum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz);
    }
}
