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

package lodsve.security.core;

import lodsve.security.annotation.Authz;
import lodsve.security.service.Authorization;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 鉴权的拦截器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:53
 */
public class AuthzInterceptor extends HandlerInterceptorAdapter {
    private Authorization authorization;

    public AuthzInterceptor(Authorization authorization) {
        this.authorization = authorization;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //获取注解
        Authz ac = method.getAnnotation(Authz.class);

        if (ac == null) {
            return super.preHandle(request, response, handler);
        }

        //判断是否有权限
        Account account = LoginAccountHolder.getCurrentAccount();
        if (account == null) {
            authorization.ifNotLogin(request, response);
        }

        if (authorization.authz(account, ac.value())) {
            return super.preHandle(request, response, handler);
        } else {
            authorization.ifNotAuth(request, response, account);
        }

        return super.preHandle(request, response, handler);
    }
}
