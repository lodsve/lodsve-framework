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

package lodsve.mybatis.exception;

import lodsve.core.exception.ApplicationException;

/**
 * mybatis异常.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/8/15 下午4:10
 */
public class MyBatisException extends ApplicationException {
    public MyBatisException(String content) {
        super(content);
    }

    public MyBatisException(Integer code, String content) {
        super(code, content);
    }

    public MyBatisException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}
