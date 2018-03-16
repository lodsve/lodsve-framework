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

package lodsve.core.properties;

import lodsve.core.properties.env.Configuration;
import org.springframework.util.Assert;

/**
 * 读取配置的profile.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016-9-22 10:37
 */
public class Profiles {
    private static Configuration profileConfig;

    static {
        Configuration frameworkConfigurationProfiles = Env.getFrameworkEnv("profiles.properties");
        profileConfig = frameworkConfigurationProfiles.subset("profiles");
    }

    /**
     * 获取profile的值
     *
     * @param profileName profile name
     * @return true/false
     */
    public static boolean getProfile(String profileName) {
        Assert.hasText(profileName, "profile name is required!");

        return Boolean.TRUE.toString().equals(profileConfig.getString(profileName, Boolean.FALSE.toString()));
    }
}
