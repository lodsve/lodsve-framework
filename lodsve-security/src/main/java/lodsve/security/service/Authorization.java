/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.security.service;

import lodsve.security.core.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-7 14:00
 */
public interface Authorization {
    /**
     * 是否登录
     *
     * @param request 请求上下文
     * @return
     */
    boolean isLogin(HttpServletRequest request);

    /**
     * 没有登录
     *
     * @param request  request
     * @param response response
     */
    void ifNotLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * 判断当前登录人是否含有给定的角色
     *
     * @param account 当前登录人
     * @param roles   允许放行的角色
     * @return true:鉴权成功  false:鉴权失败
     */
    boolean authz(Account account, String... roles);

    /**
     * 没有权限
     *
     * @param request  request
     * @param response response
     * @param account  登录用户
     */
    void ifNotAuth(HttpServletRequest request, HttpServletResponse response, Account account);

    /**
     * 获取当前登录人的ID
     *
     * @param request 请求上下文
     * @return 当前登录人ID
     */
    Account getCurrentUser(HttpServletRequest request);
}
