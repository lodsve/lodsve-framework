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

package lodsve.core.logger;

import lodsve.core.properties.Env;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * log4j的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/26 下午2:44
 */
public class Log4JConfiguration {

    public static void init() {
        Resource resource = new ClassPathResource("/META-INF/log4j.properties", Thread.currentThread().getContextClassLoader());

        if (!resource.exists()) {
            resource = Env.getFileEnv("log4j.properties");
        }

        if (resource == null || !resource.exists()) {
            throw new RuntimeException("配置文件'log4j.properties'找不到！");
        }

        Properties prop = null;
        try {
            prop = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            System.err.println("no log4j configuration file!");
        }

        PropertyConfigurator.configure(prop);
    }
}
