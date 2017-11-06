package lodsve.validate.core;

import lodsve.core.config.ProfileConfig;
import lodsve.core.utils.ObjectUtils;
import lodsve.core.utils.StringUtils;
import lodsve.validate.annotations.ValidateEntity;
import lodsve.validate.constants.ValidateConstants;
import lodsve.validate.exception.DefaultExceptionHandler;
import lodsve.validate.exception.ErrorMessage;
import lodsve.validate.exception.ExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

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

    /**
     * 异常处理类,默认使用DefaultExceptionHandler
     */
    @Autowired(required = false)
    private ExceptionHandler exceptionHandler;

    /**
     * 是否开启验证引擎?默认是开启
     */
    private boolean openValidate = ProfileConfig.getProfile("validator");
    /**
     * key-value(注解名称-beanHandler)
     */
    private Map<String, BeanHandler> beanHandlerMap;
    /**
     * 注解的集合(集合的名称)
     */
    private List<String> annotations;

    /**
     * 需要验证的类--需要验证的字段(内存中的缓存)
     */
    private Map<String, List<Field>> validateFields;

    /**
     * 验证引擎的注解所在classpath下的包路径
     */
    private static final String ANNOTATION_PATH = "lodsve.validate.annotations";
    /**
     * 验证引擎的注解对应的处理类所在classpath下的包路径
     */
    private static final String VALIDATE_HANDLER_PATH = "lodsve.validate.handler";
    /**
     * 处理类的后缀名(类名)
     */
    private static final String HANDLER_CLASS_SUFFIX = "Handler";

    /******************************************************验证引擎初始化--开始***********************************************************/

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.openValidate) {
            logger.debug("this web project is not open validate engine!");
            return;
        }

        this.exceptionHandler = this.exceptionHandler == null ? new DefaultExceptionHandler() : this.exceptionHandler;

        beanHandlerMap = new HashMap<>(16);
        validateFields = new HashMap<>(16);
        annotations = new ArrayList<>(16);

        initValidateEngine();
    }

    /**
     * 初始化验证引擎
     */
    private void initValidateEngine() throws Exception {
        String[] validateAnnotations = ValidateConstants.VALIDATE_ANNOTATIONS;
        if (validateAnnotations == null || validateAnnotations.length == 0) {
            logger.debug("there is no validate annotations in jars!");
            return;
        }
        for (String va : validateAnnotations) {
            resolveAnnotation(va);
        }
    }

    private void resolveAnnotation(String annotationName) {
        if (StringUtils.isEmpty(annotationName)) {
            return;
        }

        String packagePath = ANNOTATION_PATH + "." + annotationName;
        Class<?> annotation;
        try {
            annotation = ClassUtils.forName(packagePath, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("can't get annotation for name '{}'!", packagePath);
            logger.error(e.getMessage(), e);
            return;
        }
        String handlerPath = VALIDATE_HANDLER_PATH + "." + annotationName + HANDLER_CLASS_SUFFIX;
        Class<?> handler;
        try {
            handler = ClassUtils.forName(handlerPath, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("can't get handler for name '{}'!", handlerPath);
            logger.error(e.getMessage(), e);
            return;
        }

        if (annotation == null || handler == null || !AbstractValidateHandler.class.equals(handler.getSuperclass())) {
            logger.error("no annotation or handler!");
            return;
        }

        BeanHandler beanHandler = new BeanHandler(annotation.getSimpleName(), annotation, (AbstractValidateHandler) BeanUtils.instantiate(handler));

        // 将beanHandlers中的注解-处理类放入beanHandlerMap中(并将注解中文名放入内存中-annotations)
        this.beanHandlerMap.put(annotation.getSimpleName(), beanHandler);
        // 将所有的注解放入内存中
        this.annotations.add(annotation.getSimpleName());
    }
    /******************************************************验证引擎初始化--结束**************************************************************/
    /************************************************************神奇的分隔符***************************************************************/
    /*******************************************************验证引擎开始工作--开始***********************************************************/

    /**
     * 基于spring AOP的"环绕"验证开始
     *
     * @param proceedingJoinPoint 切面
     * @throws Exception
     */
    @Around("@annotation(lodsve.validate.core.NeedValidate)")
    public Object validate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (!this.openValidate) {
            logger.debug("this web project is not open validate engine!");
            return proceedingJoinPoint.proceed();
        }

        logger.debug("start validate..., proceedingJoinPoint is '{}'!", proceedingJoinPoint);

        //获取所有参数
        Object[] args = proceedingJoinPoint.getArgs();
        logger.debug("get args is '{}'!", args);

        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }

            Class clazz = arg.getClass();
            if (clazz.isAnnotationPresent(ValidateEntity.class)) {
                errorMessages.addAll(validateEntityFields(arg));
            }
        }

        if (CollectionUtils.isNotEmpty(errorMessages)) {
            // 处理异常
            this.exceptionHandler.doHandleException(errorMessages);
        }

        return proceedingJoinPoint.proceed();
    }

    /**
     * 对entity每个字段进行验证
     *
     * @param entity 要验证的entity
     * @return
     */
    private List<ErrorMessage> validateEntityFields(Object entity) throws Exception {
        if (entity == null) {
            logger.error("given empty entity!");
            return Collections.emptyList();
        }
        List<Field> fieldList = this.getValidateFields(entity.getClass());
        if (CollectionUtils.isEmpty(fieldList)) {
            logger.debug("given entity class is '{}' has no validate fields!", entity.getClass());
            return Collections.emptyList();
        }

        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (Field f : fieldList) {
            errorMessages.addAll(validateField(f, entity));
        }

        return errorMessages;
    }

    /**
     * 验证字段
     *
     * @param f      待验证字段
     * @param entity 待验证实体
     * @return
     */
    private List<ErrorMessage> validateField(final Field f, final Object entity) throws Exception {
        if (f == null || entity == null) {
            logger.error("given field is null or entity is null!");
            return Collections.emptyList();
        }
        Annotation[] as = f.getAnnotations();

        List<ErrorMessage> messages = new ArrayList<>();
        for (Annotation a : as) {
            BeanHandler bh = this.beanHandlerMap.get(a.annotationType().getSimpleName());
            if (bh == null) {
                continue;
            }

            AbstractValidateHandler handler = bh.getValidateHandler();
            if (handler == null) {
                logger.error("handler is null for annotation '{}'", a);
                continue;
            }
            Object value = ObjectUtils.getFieldValue(entity, f.getName());
            ErrorMessage message = handler.validate(a, value);
            if (message != null) {
                message.setClazz(entity.getClass());
                message.setField(f);
                message.setValue(value);

                messages.add(message);
            }
        }

        return messages;
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
        // 内存中不存在这个类的需要验证字段
        if (CollectionUtils.isEmpty(fieldList)) {
            fieldList = new ArrayList<>();
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

            if (CollectionUtils.isNotEmpty(fieldList)) {
                //放入内存中
                this.validateFields.put(key, fieldList);
            }
        }

        return fieldList;
    }

    /**
     * *************************************************验证引擎开始工作--结束*********************************************************
     */
}
