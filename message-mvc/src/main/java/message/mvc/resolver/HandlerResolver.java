package message.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析参数的接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-19 13:33
 */
public interface HandlerResolver<T> {
    /**
     * 解析参数
     *
     * @param request
     * @param response
     * @return
     */
    public T resolver(HttpServletRequest request, HttpServletResponse response);
}
