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

package lodsve.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * Lodsve Ext ConfigurationLoader.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/12 12:53
 */
public class LodsveExtConfigurationLoader implements ImportSelector {
    private static boolean isInit = false;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (isInit) {
            return new String[0];
        }

        isInit = true;
        List<String> imports = getConfigurations();
        return imports.toArray(new String[imports.size()]);
    }

    private List<String> getConfigurations() {
        return SpringFactoriesLoader.loadFactoryNames(Configuration.class, Thread.currentThread().getContextClassLoader());
    }
}
