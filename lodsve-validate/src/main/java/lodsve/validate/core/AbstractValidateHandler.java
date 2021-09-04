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
package lodsve.validate.core;

import lodsve.validate.constants.ValidateConstants;
import lodsve.validate.exception.ErrorMessage;
import lodsve.validate.handler.DoubleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 所有验证条件的处理类必须继承这个抽象类,以实现各自的验证方法.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午7:46
 */
public abstract class AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(DoubleHandler.class);

    /**
     * 所有继承这个类的类才会是对应注解验证类型的验证类,验证的时候都调用这个方法
     *
     * @param annotation 待验证字段的注解
     * @param value      待验证字段的值
     * @return
     */
    protected abstract ErrorMessage handle(Annotation annotation, Object value);

    /**
     * 在抽象类中进行每个验证组件都会执行的操作(判空)
     *
     * @param annotation 待验证字段的注解
     * @param value      待验证字段的值
     * @return
     */
    public ErrorMessage validate(Annotation annotation, Object value) {
        if (annotation == null) {
            logger.error("given null annotation!");
            return null;
        }

        return this.handle(annotation, value);
    }

    protected ErrorMessage getMessage(Class<? extends Annotation> annotation, Class<? extends AbstractValidateHandler> handler, String key, boolean result, Object... args) {
        if (result) {
            return null;
        }

        return new ErrorMessage(annotation, handler, ValidateConstants.getMessage(key, args));
    }
}
