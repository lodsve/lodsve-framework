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
