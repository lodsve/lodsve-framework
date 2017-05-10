package lodsve.mvc.context;

import javax.servlet.http.HttpServletRequest;

/**
 * request.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class RequestHolder {
    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }
}
