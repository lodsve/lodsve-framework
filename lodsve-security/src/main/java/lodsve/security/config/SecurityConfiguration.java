package lodsve.security.config;

import lodsve.security.annotation.resolver.CurrentAccountResolver;
import lodsve.security.core.AuthzInterceptor;
import lodsve.security.core.LoginInterceptor;
import lodsve.security.service.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * security模块的配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-2-18 14:27
 */
@Configuration
@ComponentScan(basePackages = "lodsve.security")
public class SecurityConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private Authorization authorization;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先执行 是否登录的拦截器
        registry.addInterceptor(new LoginInterceptor(authorization));
        // 后执行 是否有权限的拦截器
        registry.addInterceptor(new AuthzInterceptor(authorization));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentAccountResolver());
    }
}
