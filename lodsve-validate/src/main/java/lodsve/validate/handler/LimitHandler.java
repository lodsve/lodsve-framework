/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午8:39
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
