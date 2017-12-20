package lodsve.mvc.context;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置请求上下文内容的拦截器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:44
 */
public class WebContextInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseHolder.setResponse(response);
        RequestHolder.setRequest(request);
        SessionHolder.setSession(request.getSession());
        LocaleHolder.setLocale(request.getLocale());

        return super.preHandle(request, response, handler);
    }
}
