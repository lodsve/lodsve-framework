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
package lodsve.core.autoproperties.message;

import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
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
