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
package lodsve.web.mvc.annotation;

import java.lang.annotation.*;

/**
 * 当前台传入类似于1,2,3,4,5之类的字符串，加上此注解，将会被解析成一个List，这个注解是一些配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-2-9 18:58
 * @see lodsve.web.mvc.annotation.resolver.ParseDataHandlerMethodArgumentResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parse {
    /**
     * 参数值在request中key，默认为参数名
     *
     * @return
     */
    String value() default "";

    /**
     * list中需要的泛型
     *
     * @return
     */
    Class<?> dest();

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 分隔符，默认为[,]
     *
     * @return
     */
    String delimiters() default ",";
}
