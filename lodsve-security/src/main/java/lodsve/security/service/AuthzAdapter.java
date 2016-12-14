package lodsve.security.service;

import lodsve.security.core.Account;
import lodsve.security.exception.AuthException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权适配器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/11/19 上午11:06
 */
public class AuthzAdapter implements Authz {
    @Override
    public boolean isLogin(HttpServletRequest request) {
        return false;
    }

    @Override
    public void ifNotLogin(HttpServletRequest request, HttpServletResponse response) {
        //未登录
        throw new AuthException(105001, "not login！");
    }

    @Override
    public boolean authz(Account account, String... roles) {
        return false;
    }

    @Override
    public void ifNotAuth(HttpServletRequest request, HttpServletResponse response, Account account) {
        throw new AuthException(105002, "authz failure! user id is:{" + account.getId() + "}!");
    }

    @Override
    public Account getCurrentUser(HttpServletRequest request) {
        return null;
    }
}
