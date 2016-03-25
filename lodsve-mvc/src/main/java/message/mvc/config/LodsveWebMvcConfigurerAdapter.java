package message.mvc.config;

import message.mvc.convert.CustomMappingJackson2HttpMessageConverter;
import message.mvc.convert.CustomObjectMapper;
import message.mvc.convert.StringDateConvertFactory;
import message.mvc.resolver.BindDataHandlerMethodArgumentResolver;
import message.mvc.resolver.ParseDataHandlerMethodArgumentResolver;
import message.mvc.resolver.WebResourceDataHandlerMethodArgumentResolver;
import message.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置springMVC.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/15 下午1:22
 */
@Component
public class LodsveWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Autowired
    private ApplicationProperties properties;

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
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        CustomMappingJackson2HttpMessageConverter converter = new CustomMappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new CustomObjectMapper());
        converters.add(converter);
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**")
                .allowedHeaders("X-requested-with", "x-auth-token", "Content-Type")
                .allowedMethods("POST", "GET", "OPTIONS", "DELETE")
                .exposedHeaders("x-auth-token");
        if (properties.isDevMode()) {
            corsRegistration.allowedOrigins("*");
        } else {
            corsRegistration.allowedOrigins(properties.getFrontEndUrl(), properties.getServerUrl());
        }
    }
}
