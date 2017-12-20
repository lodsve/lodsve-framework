package lodsve.mvc.context;

import javax.servlet.http.HttpServletResponse;

/**
 * response.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class ResponseHolder {
    private final static ThreadLocal<HttpServletResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setResponse(HttpServletResponse response) {
        RESPONSE_THREAD_LOCAL.set(response);
    }

    public static HttpServletResponse getResponse() {
        return RESPONSE_THREAD_LOCAL.get();
    }

    public static void removeResponse() {
        RESPONSE_THREAD_LOCAL.remove();
    }
}
