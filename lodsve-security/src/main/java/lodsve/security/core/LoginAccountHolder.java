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

package lodsve.security.core;

/**
 * 在当前请求（即一个线程中）中保存当前登录者.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 16:50
 */
public class LoginAccountHolder {
    private final static ThreadLocal<Account> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();

    public static Account getCurrentAccount() {
        return ACCOUNT_THREAD_LOCAL.get();
    }

    public static void setCurrentAccount(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static void removeCurrentAccount() {
        ACCOUNT_THREAD_LOCAL.remove();
    }
}
