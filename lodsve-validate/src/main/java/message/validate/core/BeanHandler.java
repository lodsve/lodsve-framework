package message.validate.core;

import message.base.utils.StringUtils;

/**
 * 类似于map,放置验证注解和其对应的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:51
 */
public class BeanHandler {
    private String key;                                         //唯一标识
    private Class annotation;                                  //注解
    private ValidateHandler validateHandler;                 //其对应的处理类

    public BeanHandler(String key, Class annotation, ValidateHandler validateHandler) {
        if(StringUtils.isEmpty(key)){
            this.key = StringUtils.substring(annotation.getSimpleName(), 0, annotation.getSimpleName().lastIndexOf("_"));
        } else {
            this.key = key;
        }
        this.annotation = annotation;
        this.validateHandler = validateHandler;
    }

    public BeanHandler(Class annotation, ValidateHandler validateHandler) {
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

    public ValidateHandler getValidateHandler() {
        return validateHandler;
    }

    public void setValidateHandler(ValidateHandler validateHandler) {
        this.validateHandler = validateHandler;
    }
}
