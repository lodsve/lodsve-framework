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

package lodsve.validate.handler;

import lodsve.core.utils.StringUtils;
import lodsve.validate.annotations.Password;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码验证处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午8:54
 */
public class PasswordHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(PasswordHandler.class);

    public PasswordHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (!(value instanceof String)) {
            logger.error("is not string!");
            return getMessage(Password.class, getClass(), "password-invalid", false);
        }

        Password password = (Password) annotation;
        String regex = password.regex();
        int min = password.min();
        int max = password.max();
        int length = ((String) value).length();

        if (StringUtils.isEmpty(regex)) {
            //不通过正则表达式
            return getMessage(Password.class, getClass(), "pwd-length-error", min < max && min <= length && max >= length, min, max, length);
        } else {
            //通过正则表达式验证
            Pattern patter = Pattern.compile(regex);
            Matcher matcher = patter.matcher((String) value);
            boolean l = true;
            if (min < max)
            //并且有长度的校验
            {
                l = (min <= length && max >= length);
            }

            return getMessage(Password.class, getClass(), "pwd-regex-error", matcher.matches() && l, regex, value);
        }
    }
}
