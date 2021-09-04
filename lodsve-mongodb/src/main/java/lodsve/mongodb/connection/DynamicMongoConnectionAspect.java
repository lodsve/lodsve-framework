/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
