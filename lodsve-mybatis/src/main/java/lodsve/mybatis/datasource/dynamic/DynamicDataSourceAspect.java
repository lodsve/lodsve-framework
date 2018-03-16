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

package lodsve.mybatis.datasource.dynamic;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.datasource.annotations.DataSourceProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * aop动态切换数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午6:16
 */
@Aspect
public class DynamicDataSourceAspect {
    @Around("@annotation(lodsve.mybatis.datasource.annotations.DataSourceProvider)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSource = StringUtils.EMPTY;
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
