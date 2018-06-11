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

package lodsve.core.properties.message;

import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.properties.ParamsHome;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * i18n资源文件加载器
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-4-15 下午10:19
 */
@Component
public class MessageSourceLoader implements InitializingBean {
    /**
     * 加载了所有的资源文件信息.
     */
    @Autowired
    private ResourceBundleHolder resourceBundleHolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource[] resources = getResources();

        for (Resource r : resources) {
            this.resourceBundleHolder.loadMessageResource(r);
        }
    }

    private Resource[] getResources() {

        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();
        Resource[] propertiesResources;
        try {
            propertiesResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.properties");
        } catch (IOException e) {
            propertiesResources = new Resource[0];
        }
        Resource[] htmlResources;
        try {
            htmlResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.html");
        } catch (IOException e) {
            htmlResources = new Resource[0];
        }
        Resource[] txtResources;
        try {
            txtResources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/i18n/**/*.txt");
        } catch (IOException e) {
            txtResources = new Resource[0];
        }

        List<Resource> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(propertiesResources));
        resources.addAll(Arrays.asList(htmlResources));
        resources.addAll(Arrays.asList(txtResources));

        return resources.toArray(new Resource[resources.size()]);
    }
}
