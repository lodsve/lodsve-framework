package message.validate.core;

import message.utils.ObjectUtils;
import message.utils.StringUtils;
import message.validate.annotations.ValidateEntity;
import message.validate.constants.ValidateConstants;
import message.validate.exception.DefaultExceptionHandler;
import message.validate.exception.ExceptionHandler;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证引擎核心组件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:53
 */
@Aspect
@Component
public class ValidateEngine implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ValidateEngine.class);

    //是否开启验证引擎?默认是开启
    private boolean openValidate = true;
    //放置验证注解和其对应的处理类
    private List<BeanHandler> beanHandlers;
    //key-value(注解名称-beanHandler)
    private Map<String, BeanHandler> beanHandlerMap;
    //异常处理类,默认使用DefaultExceptionHandler
    private ExceptionHandler exceptionHandler = new DefaultExceptionHandler();
    //注解的集合(集合的名称)
    private List<String> annotations;

    /**
     * 需要验证的类--需要验证的字段(内存中的缓存)
     */
    private Map<String, List<Field>> validateFields;

    //验证引擎的注解所在classpath下的包路径
    private static final String ANNOTATION_PATH = "message.validate.annotations";
    //验证引擎的注解对应的处理类所在classpath下的包路径
    private static final String VALIDATE_HANDLER_PATH = "message.validate.handler";
    //处理类的后缀名(类名)
    private static final String HANDLER_CLASS_SUFFIX = "Handler";

    /******************************************************验证引擎初始化--开始***********************************************************/
    /**
     * 初始化验证引擎
     */
    private void initValidateEngine() throws Exception {
        this.getBeanHandler();

        putBeanHandleToMap();
    }

    /**
     * 从jar包中获取验证的beanHandler
     *
     * @throws Exception
     */
    private void getBeanHandler() {
        String[] validateAnnotations = ValidateConstants.VALIDATE_ANNOTATIONS;
        if (validateAnnotations == null || validateAnnotations.length == 0) {
            logger.debug("there is no validate annotations in jars!");
            return;
        }
        for (String va : validateAnnotations) {
            if (StringUtils.isEmpty(va)) continue;

            String packagePath = ANNOTATION_PATH + "." + va;
            Class<?> annotation = null;
            try {
                annotation = ClassUtils.forName(packagePath, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                logger.error("can't get annotation for name '{}'!", packagePath);
                logger.error(e.getMessage(), e);
                continue;
            }
            String handlerPath = VALIDATE_HANDLER_PATH + "." + va + HANDLER_CLASS_SUFFIX;
            Class<?> handler = null;
            try {
                handler = ClassUtils.forName(handlerPath, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                logger.error("can't get handler for name '{}'!", handlerPath);
                logger.error(e.getMessage(), e);
                continue;
            }

            if (annotation == null || handler == null || !ValidateHandler.class.equals(handler.getSuperclass())) {
                logger.error("no annotation or handler!");
                continue;
            }

            BeanHandler beanHandler = new BeanHandler(annotation.getSimpleName(), annotation, (ValidateHandler) BeanUtils.instantiate(handler));
            beanHandlers.add(beanHandler);
        }
    }

    /**
     * 将beanHandlers中的注解-处理类放入beanHandlerMap中(并将注解中文名放入内存中-annotations)
     */
    private void putBeanHandleToMap() {
        if (!this.beanHandlers.isEmpty()) {
            for (BeanHandler bh : this.beanHandlers) {
                if (bh.getKey() != null && bh.getAnnotation() != null && bh.getValidateHandler() != null) {
                    logger.debug("validate key is '{}', annotation is '{}', validateHandler is '{}'!", new Object[]{
                            bh.getKey(), bh.getAnnotation(), bh.getValidateHandler()
                    });
                    //TODO 需要处理这里key值
                    this.beanHandlerMap.put(bh.getKey(), bh);

                    //将所有的注解放入内存中
                    String annName = bh.getAnnotation().getSimpleName();
                    this.annotations.add(annName);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.openValidate) {
            logger.debug("this web project is not open validate engine!");
            return;
        }
        if (beanHandlers == null) {
            beanHandlers = new ArrayList<BeanHandler>();
        }
        if (beanHandlerMap == null) {
            beanHandlerMap = new HashMap<String, BeanHandler>();
        }
        if (this.validateFields == null) {
            validateFields = new HashMap<String, List<Field>>();
        }
        if (this.annotations == null) {
            this.annotations = new ArrayList<String>();
        }

        initValidateEngine();
    }

    /******************************************************验证引擎初始化--结束**************************************************************/
    /************************************************************神奇的分隔符***************************************************************/
    /*******************************************************验证引擎开始工作--开始***********************************************************/

    /**
     * 基于spring AOP的"环绕"验证开始
     *
     * @param object 待校验对象
     * @throws Exception
     */
    @Before(value = "@annotation(message.validate.core.NeedValidate) && args(object,..)", argNames = "object")
    public void validate(Object object) throws Throwable {
        if (!this.openValidate) {
            logger.debug("this web project is no open validate engine!");
        }
        if (logger.isDebugEnabled())
            logger.debug("start validate..., validate object is '{}'!", object);

        Class clazz = object.getClass();
        if (clazz.isAnnotationPresent(ValidateEntity.class)) {
            validateEntityFields(object);
        }
    }

    /**
     * 对entity每个字段进行验证
     *
     * @param entity 要验证的entity
     * @return
     */
    private boolean validateEntityFields(Object entity) throws Exception {
        if (entity == null) {
            logger.error("given empty entity!");
            return false;
        }
        List<Field> fieldList = this.getValidateFields(entity.getClass());
        if (fieldList == null || fieldList.isEmpty()) {
            logger.debug("given entity class is '{}' has no validate fields!", entity.getClass());
            return true;
        }

        boolean result = false;
        for (Field f : fieldList) {
            result = this.validateField(f, entity);
            if (!result) {
                break;
            }
        }

        return result;
    }

    /**
     * 验证字段
     *
     * @param f      待验证字段
     * @param entity 待验证实体
     * @return
     */
    private boolean validateField(final Field f, final Object entity) throws Exception {
        if (f == null || entity == null) {
            logger.error("given field is null or entity is null!");
            return false;
        }
        Annotation[] as = f.getAnnotations();
        boolean result = false;
        for (Annotation a : as) {
            //TODO 需要处理这里key值
            BeanHandler bh = this.beanHandlerMap.get(a.annotationType().getSimpleName());
            if (bh != null) {
                ValidateHandler handler = bh.getValidateHandler();
                if (handler == null) {
                    logger.error("handler is null for annotation '{}'", a);
                    //仅结束本次循环
                    continue;
                }
                Object value = ObjectUtils.getFieldValue(entity, f.getName());
                result = handler.validate(a, value);
                if (!result) {
                    logger.debug("validate field is error for class '{}'! field is '{}', value is '{}'!", new Object[]{
                            entity.getClass(), f.getName(), value
                    });
                    //执行异常处理类
                    this.exceptionHandler.doHandleException(entity.getClass(), f, value, a);
                }
            }
        }

        return result;
    }

    /**
     * 获取指定类需要验证的字段,如果内存中没有做缓存,则循环取出来,否则取内存中的
     *
     * @param clazz 指定类
     * @return
     * @throws Exception
     */
    private List<Field> getValidateFields(Class<?> clazz) throws Exception {
        if (clazz == null) {
            logger.error("given empty class!");
            return Collections.emptyList();
        }
        String key = "validate-" + clazz.getName();
        List<Field> fieldList = this.validateFields.get(key);
        //内存中不存在这个类的需要验证字段
        if (fieldList == null || fieldList.isEmpty()) {
            fieldList = new ArrayList<Field>();
            Field[] fields = ObjectUtils.getFields(BeanUtils.instantiate(clazz));
            for (Field f : fields) {
                Annotation[] ans = f.getAnnotations();
                for (Annotation a : ans) {
                    if (this.annotations.contains(a.annotationType().getSimpleName())) {
                        fieldList.add(f);
                        //这个字段只要有一个注解是符合条件的,立马放入待验证字段中
                        break;
                    }
                }
            }

            if (fieldList != null && !fieldList.isEmpty()) {
                //放入内存中
                this.validateFields.put(key, fieldList);
            }
        }

        return fieldList;
    }

    /**
     * *************************************************验证引擎开始工作--结束*********************************************************
     */

    public void setBeanHandlers(List<BeanHandler> beanHandlers) {
        this.beanHandlers = beanHandlers;
    }

    public void setValidateFields(Map<String, List<Field>> validateFields) {
        this.validateFields = validateFields;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setOpenValidate(boolean openValidate) {
        this.openValidate = openValidate;
    }
}
