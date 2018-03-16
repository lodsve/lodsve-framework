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

package lodsve.mvc.commons;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 封装HttpServletResponse
 *
 * @author sunhao(sunhao.java@gmail.com)
 */
public class WebOutput {
    private HttpServletResponse response;

    /**
     * 构造函数
     *
     * @param response
     */
    public WebOutput(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * 设置cookie
     *
     * @param name   name
     * @param value  value
     * @param maxAge 存活时间
     */
    public void setCookie(String name, String value, int maxAge) {
        Cookie c = new Cookie(name, value);
        if (maxAge > 0) {
            c.setMaxAge(maxAge);
        }
        c.setPath("/");
        this.response.addCookie(c);
    }

    public void setContentType(String contentType) {
        this.response.setContentType(contentType);
    }

    public void setContentType(String contextType, String charset) {
        if (charset == null) {
            this.response.setContentType(contextType);
        } else {
            this.response.setContentType(contextType + "; charset=" + charset);
        }
    }
}
