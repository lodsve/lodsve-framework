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

package lodsve.web.utils;

import lodsve.core.utils.ObjectUtils;
import lodsve.core.utils.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie处理工具类
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-7-10 下午02:41:14
 */
public class CookieUtils {

    /**
     * 私有化构造器
     */
    private CookieUtils() {
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie的名称
     * @param value    cookie的值
     * @throws Exception
     */
    public static void setCookie(final HttpServletResponse response, final String name, final String value) {
        setCookie(response, name, value, "/", "", "", -1);
    }

    /**
     * 移除cookie
     *
     * @param response
     * @param name     cookie的名称
     * @throws Exception
     */
    public static void removeCookie(final HttpServletResponse response, final String name) {
        setCookie(response, name, "", "/", "", "", 0);
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie的名称
     * @param value    cookie的值
     * @param path     cookie的路径,不设置的话为当前路径
     * @param comment  cookie的备注
     * @param domain   cookie的共享域名,参考http://www.iteye.com/topic/34400
     * @param expired  cookie的生存周期时间,生存周期默认时间为秒，
     *                 如果设置为负值的话，则为浏览器进程Cookie(内存中保存)，关闭浏览器就失效。
     */
    public static void setCookie(final HttpServletResponse response, final String name, final String value,
                                 String path, String comment, String domain, final int expired) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setComment(comment);
        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(expired);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie的值
     *
     * @param request
     * @param name    cookie的名称
     * @return
     * @throws Exception
     */
    public static String getCookieValue(final HttpServletRequest request, final String name) {
        if (ObjectUtils.isEmpty(request)) {
            return StringUtils.EMPTY;
        }
        String cValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(name, cookie.getName())) {
                    cValue = cookie.getValue();
                    break;
                }
            }
        }

        return cValue;
    }

}
