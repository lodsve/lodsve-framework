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
package lodsve.core;

import lodsve.core.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * 获取框架版本号，即maven中的version.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/11 下午11:07
 */
public final class LodsveVersion {
    private LodsveVersion() {
    }

    public static String getVersion() {
        return LodsveVersion.class.getPackage().getImplementationVersion();
    }

    public static String getBuilter() {
        return getManifestAttr("Built-By");
    }

    private static String getManifestAttr(String name) {
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = LodsveVersion.class.getClassLoader();
            Enumeration<URL> manifestResources = classLoader.getResources("META-INF/MANIFEST.MF");
            while (manifestResources.hasMoreElements()) {
                inputStream = manifestResources.nextElement().openStream();
                Manifest manifest = new Manifest(inputStream);
                String builter = manifest.getMainAttributes().getValue(name);

                if (StringUtils.isNotBlank(builter)) {
                    return builter;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return StringUtils.EMPTY;
    }
}
