/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.core.autoproperties.env;

import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.io.support.LodsveResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件解析.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-17 下午9:44
 */
public class EnvLoader {
    private static final Properties env = new Properties();

    private EnvLoader() {
    }

    public static Properties getFileEnvs(Resource... resources) {
        Properties prop = new Properties();

        loadProperties(prop, Arrays.asList(resources));

        return prop;
    }

    public static Properties getEnvs() {
        return env;
    }

    private static void loadProperties(Properties prop, List<Resource> resources) {
        for (Resource resource : resources) {
            try {
                PropertiesLoaderUtils.fillProperties(prop, new EncodedResource(resource, "UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException("load配置文件发生异常");
            }
        }
    }

    public static Resource getFileEnv(String fileName) {
        ResourceLoader loader = new LodsveResourceLoader();

        return loader.getResource(ParamsHome.getInstance().getParamsRoot() + File.separator + "files" + File.separator + fileName);
    }

    public static Resource getFrameworkEnv(String fileName) {
        ResourceLoader loader = new LodsveResourceLoader();

        return loader.getResource(ParamsHome.getInstance().getParamsRoot() + File.separator + "framework" + File.separator + fileName);
    }

    public static void init() {
        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        try {
            resources.addAll(Arrays.asList(resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/*.properties")));
            resources.addAll(Arrays.asList(resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/framework/*.properties")));
        } catch (IOException e) {
            return;
        }

        loadProperties(env, resources);

        // 获取覆盖的值
        ParamsHome.getInstance().coveredWithExtResource(env);

        env.put("params.root", ParamsHome.getInstance().getParamsRoot());
    }
}
