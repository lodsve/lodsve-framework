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
package lodsve.core.autoproperties;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 读取配置的profile.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-9-22 10:37
 */
@Component
public class Profiles {
    private static Environment environment;

    /**
     * 初始化环境
     *
     * @param environment environment
     */
    public static void init(Environment environment) {
        Profiles.environment = environment;
    }

    /**
     * 获取profile的值
     *
     * @param profileName profile name
     * @return true/false
     */
    public static boolean getProfile(String profileName) {
        Assert.hasText(profileName, "profile name is required!");

        return environment.acceptsProfiles(profileName);
    }
}
