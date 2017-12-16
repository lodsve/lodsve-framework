package lodsve.mongodb.connection;

import lodsve.core.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * aop动态切换数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午6:16
 */
@Aspect
@Component
public class DynamicMongoConnectionAspect {
    @Around("@annotation(lodsve.mongodb.connection.MongoSourceProvider)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String mongoSource = StringUtils.EMPTY;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method pointMethod = signature.getMethod();

        if (pointMethod.isAnnotationPresent(MongoSourceProvider.class)) {
            MongoSourceProvider annotation = pointMethod.getAnnotation(MongoSourceProvider.class);
            // 取出注解中的数据源名
            mongoSource = annotation.value();
        }

        MongoDataSourceHolder.set(mongoSource);

        try {
            return joinPoint.proceed();
        } finally {
            MongoDataSourceHolder.clear();
        }
    }
}
