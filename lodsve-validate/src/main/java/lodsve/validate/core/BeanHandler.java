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

package lodsve.validate.core;

import lodsve.core.utils.StringUtils;

/**
 * 类似于map,放置验证注解和其对应的处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-11-26 下午9:51
 */
public class BeanHandler {
    /**
     * 唯一标识
     */
    private String key;
    /**
     * 注解
     */
    private Class annotation;
    /**
     * 其对应的处理类
     */
    private AbstractValidateHandler validateHandler;

    public BeanHandler(String key, Class annotation, AbstractValidateHandler validateHandler) {
        if (StringUtils.isEmpty(key)) {
            this.key = StringUtils.substring(annotation.getSimpleName(), 0, annotation.getSimpleName().lastIndexOf("_"));
        } else {
            this.key = key;
        }
        this.annotation = annotation;
        this.validateHandler = validateHandler;
    }

    public BeanHandler(Class annotation, AbstractValidateHandler validateHandler) {
        this.key = StringUtils.substring(annotation.getSimpleName(), 0, annotation.getSimpleName().lastIndexOf("_"));
        this.annotation = annotation;
        this.validateHandler = validateHandler;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Class getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Class annotation) {
        this.annotation = annotation;
    }

    public AbstractValidateHandler getValidateHandler() {
        return validateHandler;
    }

    public void setValidateHandler(AbstractValidateHandler validateHandler) {
        this.validateHandler = validateHandler;
    }
}
