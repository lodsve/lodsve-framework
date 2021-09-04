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
