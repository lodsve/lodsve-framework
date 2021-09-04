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

import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.core.configuration.properties.ApplicationProperties;
import lodsve.web.mvc.annotation.resolver.BindDataHandlerMethodArgumentResolver;
import lodsve.web.mvc.annotation.resolver.ParseDataHandlerMethodArgumentResolver;
import lodsve.web.mvc.annotation.resolver.WebResourceDataHandlerMethodArgumentResolver;
import lodsve.web.mvc.convert.EnumCodeConverterFactory;
import lodsve.web.mvc.convert.StringDateConvertFactory;
import lodsve.web.mvc.properties.ServerProperties;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置springMVC.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/8/15 下午1:22
 */
public class LodsveWebMvcConfigurerAdapter implements WebMvcConfigurer {
    private final ServerProperties properties;
    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper;

    public LodsveWebMvcConfigurerAdapter(ServerProperties properties, ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.applicationProperties = applicationProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        BindDataHandlerMethodArgumentResolver bindDataHandlerMethodArgumentResolver = new BindDataHandlerMethodArgumentResolver();
        ParseDataHandlerMethodArgumentResolver parseDataHandlerMethodArgumentResolver = new ParseDataHandlerMethodArgumentResolver();
        WebResourceDataHandlerMethodArgumentResolver webResourceDataHandlerMethodArgumentResolver = new WebResourceDataHandlerMethodArgumentResolver();

        argumentResolvers.add(bindDataHandlerMethodArgumentResolver);
        argumentResolvers.add(parseDataHandlerMethodArgumentResolver);
        argumentResolvers.add(webResourceDataHandlerMethodArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringDateConvertFactory());
        registry.addConverterFactory(new EnumCodeConverterFactory());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true)
            .favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .useRegisteredExtensionsOnly(true)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("html", MediaType.TEXT_HTML);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**")
            .allowedHeaders("X-requested-with", "x-auth-token", "Content-Type")
            .allowedMethods("POST", "GET", "OPTIONS", "DELETE")
            .exposedHeaders("x-auth-token");
        if (applicationProperties.isDevMode()) {
            corsRegistration.allowedOrigins("*");
        } else {
            corsRegistration.allowedOrigins(properties.getFrontEndUrl(), properties.getServerUrl());
        }
    }
}
