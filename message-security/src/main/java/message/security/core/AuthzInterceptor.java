package message.security.core;

import message.security.annotation.NeedAuthz;
import message.security.pojo.Account;
import message.base.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 鉴权的拦截器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:53
 */
@Component
public class AuthzInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private message.security.service.Authz authz;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //获取注解
        NeedAuthz ac = method.getAnnotation(NeedAuthz.class);

        if (ac == null) {
            return super.preHandle(request, response, handler);
        }

        Account account = this.authz.getLoginAccount(request);
        if (account == null) {
            //未登录
            throw new RuntimeException("未登录！");
        }

        boolean authz = this.authz.authz(account.getLoginName(), ac.roles());

        if (authz) {
            return super.preHandle(request, response, handler);
        } else {
            throw new RuntimeException(String.format("用户%s没有权限访问此url:%s,指定角色code为:%s",
                    account.getLoginName(), request.getContextPath(), StringUtils.join(ac.roles(), ",")));
        }
    }
}
