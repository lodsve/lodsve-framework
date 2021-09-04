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
package lodsve.core.autoproperties.relaxedbind.annotations;

import java.lang.annotation.*;

/**
 * 配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/12/30 上午9:28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ConfigurationProperties {
    /**
     * The name prefix of the properties that are valid to bind to this object.
     *
     * @return the name prefix of the properties to bind
     */
    String prefix() default "";

    /**
     * Optionally provide explicit resource locations to bind to. By default the
     * configuration at these specified locations will be merged with the default
     * configuration.
     *
     * @return the path (or paths) of resources to bind to
     */
    String[] locations() default {};
}
