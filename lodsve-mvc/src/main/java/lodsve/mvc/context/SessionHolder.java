package lodsve.mvc.context;

import javax.servlet.http.HttpSession;

/**
 * session.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class SessionHolder {
    private static ThreadLocal<HttpSession> sessionThreadLocal = new ThreadLocal<>();

    public static void setSession(HttpSession session) {
        sessionThreadLocal.set(session);
    }

    public static HttpSession getSession() {
        return sessionThreadLocal.get();
    }
}
