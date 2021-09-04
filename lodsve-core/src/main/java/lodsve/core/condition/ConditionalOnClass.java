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
package lodsve.core.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link Conditional} that only matches when the specified classes are on the classpath.
 *
 * @author Phillip Webb
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

    /**
     * The classes that must be present. Since this annotation is parsed by loading class
     * bytecode, it is safe to specify classes here that may ultimately not be on the
     * classpath, only if this annotation is directly on the affected component and
     * <b>not</b> if this annotation is used as a composed, meta-annotation. In order to
     * use this annotation as a meta-annotation, only use the {@link #name} attribute.
     *
     * @return the classes that must be present
     */
    Class<?>[] value() default {};

    /**
     * The classes names that must be present.
     *
     * @return the class names that must be present.
     */
    String[] name() default {};

}
