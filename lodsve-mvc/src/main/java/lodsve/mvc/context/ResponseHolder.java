package lodsve.mvc.context;

import javax.servlet.http.HttpServletResponse;

/**
 * response.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class ResponseHolder {
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    public static void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }

    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }
}
