package lodsve.mvc.config;

import lodsve.core.configuration.ApplicationProperties;
import lodsve.mvc.context.WebContextInterceptor;
import lodsve.mvc.convert.EnumCodeConverterFactory;
import lodsve.mvc.convert.StringDateConvertFactory;
import lodsve.mvc.json.CustomObjectMapper;
import lodsve.mvc.resolver.BindDataHandlerMethodArgumentResolver;
import lodsve.mvc.resolver.ParseDataHandlerMethodArgumentResolver;
import lodsve.mvc.resolver.WebResourceDataHandlerMethodArgumentResolver;
import lodsve.properties.WebProperties;
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
 * @version V1.0, 15/8/15 下午1:22
 */
public class LodsveWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    private WebProperties properties;
    private ApplicationProperties applicationProperties;

    public LodsveWebMvcConfigurerAdapter(WebProperties properties, ApplicationProperties applicationProperties) {
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
            corsRegistration.allowedOrigins(properties.getServer().getFrontEndUrl(), properties.getServer().getServerUrl());
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebContextInterceptor());
    }
}
