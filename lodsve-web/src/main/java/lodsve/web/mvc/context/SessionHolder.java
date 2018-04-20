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

package lodsve.web.mvc.context;

import javax.servlet.http.HttpSession;

/**
 * session.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 2017/5/8 下午2:37
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
