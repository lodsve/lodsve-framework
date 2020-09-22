/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.validate.handler;

import lodsve.core.utils.NumberUtils;
import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Integer;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 整数验证的处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午8:28
 */
public class IntegerHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(IntegerHandler.class);

    public IntegerHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled()) {
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);
        }

        if (!NumberUtils.isNumber(value + "")) {
            logger.error("it is not Integer, '{}'!", value);
            return getMessage(Integer.class, getClass(), "integer-invalid", false);
        }

        lodsve.validate.annotations.Integer i = (lodsve.validate.annotations.Integer) annotation;
        boolean result;
        int min = i.min();
        int max = i.max();

        if (min < max) {
            int v = java.lang.Integer.valueOf(value + "");
            logger.debug("get validate min is '{}', max is '{}', value is '{}'", new int[]{min, max, v});
            result = v >= min && v <= max && ValidateUtils.isInteger(value + "");
            return getMessage(Integer.class, this.getClass(), "integer-length", result, min, max);
        }

        return null;
    }
}
