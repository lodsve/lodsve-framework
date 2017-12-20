package lodsve.mvc.context;

import javax.servlet.http.HttpSession;

/**
 * session.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/5/8 下午2:37
 */
public class SessionHolder {
    private final static ThreadLocal<HttpSession> SESSION_THREAD_LOCAL = new ThreadLocal<>();

    public static void setSession(HttpSession session) {
        SESSION_THREAD_LOCAL.set(session);
    }

    public static HttpSession getSession() {
        return SESSION_THREAD_LOCAL.get();
    }

    public static void removeSession() {
        SESSION_THREAD_LOCAL.remove();
    }
}
