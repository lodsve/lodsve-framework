package message.mvc.config;

import message.mvc.web.convert.CustomMappingJackson2HttpMessageConverter;
import message.mvc.web.convert.CustomObjectMapper;
import message.mvc.web.convert.EnumCodeConverterFactory;
import message.mvc.web.convert.StringDateConvertFactory;
import message.mvc.web.resolver.BindDataHandlerMethodArgumentResolver;
import message.mvc.web.resolver.ParseDataHandlerMethodArgumentResolver;
import message.mvc.web.resolver.WebResourceDataHandlerMethodArgumentResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置springMVC.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/15 下午1:22
 */
@Configuration
@ComponentScan(basePackages = {"message.mvc.web"})
public class CosmosWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

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
        StringDateConvertFactory stringDateConvertFactory = new StringDateConvertFactory();
        EnumCodeConverterFactory enumCodeConverterFactory = new EnumCodeConverterFactory();

        registry.addConverterFactory(stringDateConvertFactory);
        registry.addConverterFactory(enumCodeConverterFactory);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        CustomMappingJackson2HttpMessageConverter converter = new CustomMappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new CustomObjectMapper());
        converters.add(converter);
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/swagger/").setCachePeriod(31556926);
    }
}
