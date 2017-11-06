package lodsve.validate.core;

import lodsve.core.utils.StringUtils;

/**
 * 类似于map,放置验证注解和其对应的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:51
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
