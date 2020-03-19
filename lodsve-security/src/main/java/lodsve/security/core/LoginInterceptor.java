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

package lodsve.security.core;

import lodsve.security.annotation.Authn;
import lodsve.security.service.Authorization;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 判断是否登录的拦截器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-7 16:46
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private Authorization authorization;

    public LoginInterceptor(Authorization authorization) {
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
        Authn login = method.getAnnotation(Authn.class);
        if (login == null) {
            //不需要
            return super.preHandle(request, response, handler);
        }

        boolean isLogin = authorization.isLogin(request);
        if (isLogin) {
            LoginAccountHolder.setCurrentAccount(authorization.getCurrentUser(request));
            return super.preHandle(request, response, handler);
        } else {
            authorization.ifNotLogin(request, response);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("loginAccount", LoginAccountHolder.getCurrentAccount());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        request.setAttribute("loginAccount", LoginAccountHolder.getCurrentAccount());
        super.afterCompletion(request, response, handler, ex);
    }
}
