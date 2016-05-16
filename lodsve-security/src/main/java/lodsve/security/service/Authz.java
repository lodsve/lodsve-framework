package lodsve.security.service;

import lodsve.security.core.Account;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:00
 */
public interface Authz {
    /**
     * 是否登录
     *
     * @param request 请求上下文
     * @return
     */
    boolean isLogin(HttpServletRequest request);

    /**
     * 判断当前登录人是否含有给定的角色
     *
     * @param account 当前登录人
     * @param roles   允许放行的角色
     * @return true:鉴权成功  false:鉴权失败
     */
    boolean authz(Account account, String... roles);

    /**
     * 获取当前登录人的ID
     *
     * @param request 请求上下文
     * @return 当前登录人ID
     */
    Account getCurrentUser(HttpServletRequest request);
}
