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
