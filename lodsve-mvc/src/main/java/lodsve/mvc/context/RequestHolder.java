package lodsve.mvc.context;

import javax.servlet.http.HttpServletRequest;

/**
 * request.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class RequestHolder {
    private final static ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static HttpServletRequest getRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void removeRequest() {
        REQUEST_THREAD_LOCAL.remove();
    }
}
