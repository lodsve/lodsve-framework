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
