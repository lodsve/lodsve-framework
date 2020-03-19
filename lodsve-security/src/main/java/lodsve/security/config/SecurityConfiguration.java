/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 14:27
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
