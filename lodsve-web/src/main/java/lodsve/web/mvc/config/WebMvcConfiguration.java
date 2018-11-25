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

package lodsve.web.mvc.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lodsve.core.configuration.ApplicationProperties;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.web.mvc.debug.DebugRequestListener;
import lodsve.web.mvc.json.EnumDeserializer;
import lodsve.web.mvc.json.EnumSerializer;
import lodsve.web.mvc.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

/**
 * web mvc 配置,扫描包路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/28 上午10:58
 */
@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class WebMvcConfiguration {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private ServerProperties properties;

    @Bean
    public DefaultServletHttpRequestHandler defaultServletHttpRequestHandler() {
        return new DefaultServletHttpRequestHandler();
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, String> urlMap = new ManagedMap<>();
        urlMap.put("/**", "defaultServletHttpRequestHandler");
        simpleUrlHandlerMapping.setUrlMap(urlMap);

        return simpleUrlHandlerMapping;
    }

    @Bean
    public DebugRequestListener debugRequestListener() {
        return new DebugRequestListener(applicationProperties.isDevMode(), properties);
    }

    @Bean
    public LodsveWebMvcConfigurerAdapter lodsveWebMvcConfigurerAdapter(ObjectMapper objectMapper) {
        return new LodsveWebMvcConfigurerAdapter(properties, applicationProperties, objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

        // 序列化枚举时的处理
        SimpleModule serializerModule = new SimpleModule("enumSerializer");
        serializerModule.addSerializer(Enum.class, new EnumSerializer());
        objectMapper.registerModule(serializerModule);

        // 反序列化枚举时的处理
        SimpleModule deserializerModule = new SimpleModule("enumDeserializer");
        deserializerModule.addDeserializer(Enum.class, new EnumDeserializer());
        objectMapper.registerModule(deserializerModule);

        //日期的处理
        //默认是将日期类型转换为yyyy-MM-dd HH:mm
        //如果需要自定义的，则在pojo对象的Date类型上加上注解
        //@com.fasterxml.jackson.annotation.JsonFormat(pattern = "时间格式化")
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        return objectMapper;
    }
}
