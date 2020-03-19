/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/14 下午6:16
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
