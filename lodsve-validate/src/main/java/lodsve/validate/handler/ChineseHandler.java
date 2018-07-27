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

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Chinese;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static lodsve.core.utils.ValidateUtils.isChinese;

/**
 * 中文验证的处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午7:48
 */
public class ChineseHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChineseHandler.class);

    public ChineseHandler() throws IOException {
        super();
    }

    @Override
    public ErrorMessage handle(Annotation annotation, Object value) {
        Chinese c = (Chinese) annotation;
        int min = c.min();
        int max = c.max();

        boolean result;
        if (min >= max) {
            logger.debug("no length limit!");
            result = ValidateUtils.isChinese((String) value);
            return getMessage(Chinese.class, this.getClass(), "chinese-invalid", result);
        } else {
            int length = ((String) value).length();
            logger.debug("get validate min is '{}', max is '{}', value length is '{}'", min, max, length);
            result = min <= length && max >= length && isChinese((String) value);
            return getMessage(Chinese.class, this.getClass(), "chinese-length", result, min, max, length);
        }
    }
}
