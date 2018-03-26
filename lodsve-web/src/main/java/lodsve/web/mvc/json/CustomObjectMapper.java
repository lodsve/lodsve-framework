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

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lodsve.web.mvc.convert.EnumDeserializer;
import lodsve.web.mvc.convert.EnumSerializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 设置Jackson序列化及反序列化时的一些规则.<br/>
 * <ol>
 * <li>设置序列化,反序列化时间时的时区</li>
 * <li>设置序列化枚举项时,将枚举转成<code>{"code":"","title":""}</code></li>
 * <li>设置序列化时间类型时默认的时间格式</li>
 * </ol>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/20 上午5:35
 */
public class CustomObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        //使用默认时区
        BaseSettings baseSettings = new BaseSettings(new BasicClassIntrospector(),
                DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(),
                null, null, null,
                Locale.getDefault(),
                TimeZone.getDefault(),
                Base64Variants.getDefaultVariant()
        );

        _serializationConfig = new SerializationConfig(baseSettings, _subtypeResolver, _mixInAnnotations);
        _deserializationConfig = new DeserializationConfig(baseSettings, _subtypeResolver, _mixInAnnotations);
        configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

        // 序列化枚举时的处理
        SimpleModule serializerModule = new SimpleModule("enumSerializer");
        serializerModule.addSerializer(Enum.class, new EnumSerializer());
        registerModule(serializerModule);

        // 反序列化枚举时的处理
        SimpleModule deserializerModule = new SimpleModule("enumDeserializer");
        deserializerModule.addDeserializer(Enum.class, new EnumDeserializer());
        registerModule(deserializerModule);

        //日期的处理
        //默认是将日期类型转换为yyyy-MM-dd HH:mm
        //如果需要自定义的，则在pojo对象的Date类型上加上注解
        //@com.fasterxml.jackson.annotation.JsonFormat(pattern = "时间格式化")
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }
}
