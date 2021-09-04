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
import lodsve.validate.annotations.Telephone;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 电话号码验证处理类(固话).
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午9:47
 */
public class TelephoneHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelephoneHandler.class);

    public TelephoneHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled()) {
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);
        }

        return getMessage(Telephone.class, getClass(), "telephone-invalid", ValidateUtils.isPhone((String) value));
    }
}
