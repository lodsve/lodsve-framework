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
package lodsve.core.context;

import lodsve.core.utils.StringUtils;
import org.springframework.util.Assert;

/**
 * 所有main方法继承此类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/20 上午10:17
 */
public class SpringSupport {
    protected SpringSupport() {
        Assert.isTrue(!(StringUtils.isBlank(supportConfigLocation()) && null == supportJavaConfig()));

        if (null != supportJavaConfig()) {
            new SpringContext.Builder().config(supportJavaConfig()).bean(this).build();
        } else {
            new SpringContext.Builder().config(supportConfigLocation()).bean(this).build();
        }
    }

    /**
     * 重写此方法，提供spring配置文件的路径，否则使用默认的配置文件
     *
     * @return spring配置文件的路径
     */
    public String supportConfigLocation() {
        return "";
    }

    /**
     * 重写此方法，提供spring JavaConfig配置Class，否则抛出异常
     *
     * @return spring JavaConfig配置Class
     */
    public Class<?> supportJavaConfig() {
        return null;
    }
}
