package lodsve.security.core;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lodsve.security.annotation.NeedLogin;
import lodsve.security.exception.AuthException;
import lodsve.security.service.Authz;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 判断是否登录的拦截器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 16:46
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private Authz authz;

    public LoginInterceptor(Authz authz) {
        this.authz = authz;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //获取注解
        NeedLogin login = method.getAnnotation(NeedLogin.class);
        if (login == null) {
            //不需要
            return super.preHandle(request, response, handler);
        }

        boolean isLogin = this.authz.isLogin(request);
        if (isLogin) {
            LoginAccountHolder.setCurrentAccount(this.authz.getCurrentUser(request));
            return super.preHandle(request, response, handler);
        } else {
            //未登录
            throw new AuthException(105001, "not login！");
        }
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
