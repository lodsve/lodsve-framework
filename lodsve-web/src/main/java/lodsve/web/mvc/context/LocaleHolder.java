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

import java.util.Locale;

/**
 * 语言.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-6-22 下午3:11
 */
public class LocaleHolder {
    private final static ThreadLocal<Locale> LOCALE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setLocale(Locale locale){
        LOCALE_THREAD_LOCAL.set(locale);
    }

    public static Locale getLocale(){
        return LOCALE_THREAD_LOCAL.get();
    }

    public static void removeLocale() {
        LOCALE_THREAD_LOCAL.remove();
    }
}
