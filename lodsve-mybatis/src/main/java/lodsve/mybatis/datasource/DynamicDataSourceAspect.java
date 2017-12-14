package lodsve.mybatis.datasource;

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
public class DynamicDataSourceAspect {
    @Around("@annotation(lodsve.mybatis.datasource.DataSourceProvider)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSource = "";
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(DataSourceProvider.class)) {
            DataSourceProvider annotation = method.getAnnotation(DataSourceProvider.class);
            // 取出注解中的数据源名
            dataSource = annotation.value();
        }

        DataSourceHolder.set(dataSource);

        try {
            return point.proceed();
        } finally {
            DataSourceHolder.clear();
        }
    }
}
