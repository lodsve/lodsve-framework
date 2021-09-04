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
package lodsve.validate.exception;

import lodsve.validate.core.AbstractValidateHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 校验错误信息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/9/20 下午7:35
 */
public class ErrorMessage {
    private Class<?> clazz;
    private Field field;
    private Object value;
    private Class<? extends Annotation> annotation;
    private Class<? extends AbstractValidateHandler> handler;
    private String message;

    public ErrorMessage(Class<? extends Annotation> annotation, Class<? extends AbstractValidateHandler> handler, String message) {
        this.annotation = annotation;
        this.handler = handler;
        this.message = message;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Class<? extends AbstractValidateHandler> getHandler() {
        return handler;
    }

    public void setHandler(Class<? extends AbstractValidateHandler> handler) {
        this.handler = handler;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
