/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
import lodsve.validate.annotations.Regex;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午9:37
 */
public class RegexHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegexHandler.class);

    public RegexHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (!(value instanceof String)) {
            logger.error("is not string!");
            return getMessage(Regex.class, getClass(), "regex-error", false);
        }
        Regex regex = (Regex) annotation;
        String regexPattern = regex.regex();
        if (StringUtils.isEmpty(regexPattern)) {
            logger.error("given empty regex string!");
            throw new RuntimeException("regex pattern string is null!");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the regex pattern is '{}'!", regexPattern);
        }

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher((String) value);

        return getMessage(Regex.class, getClass(), "regex-not-match", matcher.matches(), regex, value);
    }
}
