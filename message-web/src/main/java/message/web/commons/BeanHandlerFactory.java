package message.web.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 每次请求往controller中注入的参数的值.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-29 上午12:18
 */
public abstract interface BeanHandlerFactory {
    public abstract Object getValue(HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
