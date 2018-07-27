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

package lodsve.core.properties.profile;

import lodsve.core.properties.Env;
import lodsve.core.properties.Profiles;
import lodsve.core.properties.env.Configuration;
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
