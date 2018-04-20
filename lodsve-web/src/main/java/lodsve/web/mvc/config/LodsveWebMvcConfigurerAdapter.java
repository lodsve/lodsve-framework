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

import lodsve.core.configuration.ApplicationProperties;
import lodsve.web.mvc.context.WebContextInterceptor;
import lodsve.web.mvc.convert.EnumCodeConverterFactory;
import lodsve.web.mvc.convert.StringDateConvertFactory;
import lodsve.web.mvc.json.CustomObjectMapper;
import lodsve.web.mvc.properties.ServerProperties;
import lodsve.web.mvc.annotation.resolver.BindDataHandlerMethodArgumentResolver;
import lodsve.web.mvc.annotation.resolver.ParseDataHandlerMethodArgumentResolver;
import lodsve.web.mvc.annotation.resolver.WebResourceDataHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * 配置springMVC.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 15/8/15 下午1:22
 */
public class LodsveWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    private ServerProperties properties;
    private ApplicationProperties applicationProperties;

    public LodsveWebMvcConfigurerAdapter(ServerProperties properties, ApplicationProperties applicationProperties) {
        this.properties = properties;
        this.applicationProperties = applicationProperties;
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
        converter.setObjectMapper(new CustomObjectMapper());
        converters.add(converter);
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).
                favorParameter(true).
                parameterName("mediaType").
                ignoreAcceptHeader(true).
                useJaf(false).
                defaultContentType(MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON).
                mediaType("html", MediaType.TEXT_HTML);
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebContextInterceptor());
    }
}
