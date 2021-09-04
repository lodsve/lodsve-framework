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
