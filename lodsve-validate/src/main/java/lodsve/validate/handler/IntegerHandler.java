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
