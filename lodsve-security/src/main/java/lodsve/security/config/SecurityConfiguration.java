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
