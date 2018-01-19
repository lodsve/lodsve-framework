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

import lodsve.validate.annotations.Limit;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 长度验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:39
 */
public class LimitHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(LimitHandler.class);

    public LimitHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        int length = 0;
        if (value instanceof String) {
            //字符串
            length = ((String) value).length();
        } else if (value instanceof Map) {
            //map
            length = ((Map) value).size();
        } else if (value instanceof Collection) {
            //集合
            length = ((Collection) value).size();
        } else if (value.getClass().isArray()) {
            //数组
            length = Array.getLength(value);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("value's type is '{}', its length is '{}'", value.getClass().getCanonicalName(), length);
        }

        Limit limit = (Limit) annotation;
        int min = limit.min();
        int max = limit.max();
        if (min < max) {
            return getMessage(Limit.class, getClass(), "limit-error", min <= length && length <= max, min, max, length);
        }

        return null;
    }
}
