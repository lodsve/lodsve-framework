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
package lodsve.core.autoproperties.profile;

import lodsve.core.autoproperties.Env;
import lodsve.core.autoproperties.Profiles;
import lodsve.core.autoproperties.env.Configuration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 读取配置的profile.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/30 下午3:20
 */
public class ProfileInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Map<String, Boolean> ALL_PROFILES = new HashMap<>(16);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment configEnv = applicationContext.getEnvironment();

        Configuration profileConfig = Env.getFrameworkEnv("profiles.properties");
        Set<String> profiles = profileConfig.subset("profiles").getKeys();

        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }

        for (String profile : profiles) {
            String profileName = "profiles." + profile;
            boolean profileValue = profileConfig.getBoolean(profileName, false);
            ProfileInitializer.ALL_PROFILES.put(profileName, profileValue);
            if (profileValue) {
                configEnv.addActiveProfile(profile);
            }
        }

        Profiles.init(configEnv);
    }

    public static Map<String, Boolean> getAllProfiles() {
        return ALL_PROFILES;
    }
}
