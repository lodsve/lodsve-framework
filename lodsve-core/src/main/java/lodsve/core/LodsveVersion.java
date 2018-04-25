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
 * @author sunhao(sunhao.java @ gmail.com)
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
