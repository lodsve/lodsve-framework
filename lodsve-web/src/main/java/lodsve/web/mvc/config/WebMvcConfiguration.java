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
package lodsve.web.mvc.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnProperty;
import lodsve.core.configuration.properties.ApplicationProperties;
import lodsve.web.mvc.debug.DebugRequestAspect;
import lodsve.web.mvc.json.CodeableEnumDeserializer;
import lodsve.web.mvc.json.CodeableEnumSerializer;
import lodsve.web.mvc.properties.ServerProperties;
import lodsve.web.utils.RestUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import java.text.SimpleDateFormat;
import java.util.List;
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
    public LodsveWebMvcConfigurerAdapter lodsveWebMvcConfigurerAdapter(ObjectMapper objectMapper) {
        return new LodsveWebMvcConfigurerAdapter(properties, applicationProperties, objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setTimeZone(TimeZone.getDefault());

        // 序列化枚举时的处理
        SimpleModule serializerModule = new SimpleModule("codeableEnumSerializer");
        serializerModule.addSerializer(Enum.class, new CodeableEnumSerializer());
        objectMapper.registerModule(serializerModule);

        // 反序列化枚举时的处理
        SimpleModule deserializerModule = new SimpleModule("codeableEnumDeserializer");
        deserializerModule.addDeserializer(Enum.class, new CodeableEnumDeserializer());
        objectMapper.registerModule(deserializerModule);

        //日期的处理
        //默认是将日期类型转换为yyyy-MM-dd HH:mm
        //如果需要自定义的，则在pojo对象的Date类型上加上注解
        //@com.fasterxml.jackson.annotation.JsonFormat(pattern = "时间格式化")
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : messageConverters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                messageConverters.remove(converter);

                MappingJackson2HttpMessageConverter newConverter = new MappingJackson2HttpMessageConverter();
                newConverter.setObjectMapper(objectMapper);
                messageConverters.add(newConverter);
            }
        }
        restTemplate.setMessageConverters(messageConverters);

        RestUtils.setRestTemplate(restTemplate);

        return restTemplate;
    }

    @Bean
    @Order(1)
    @ConditionalOnClass(Aspect.class)
    @ConditionalOnProperty(clazz = ApplicationProperties.class, key = "devMode", value = "true")
    public DebugRequestAspect debugRequestAspect(ObjectMapper objectMapper) {
        return new DebugRequestAspect(objectMapper, properties.getDebug());
    }
}
