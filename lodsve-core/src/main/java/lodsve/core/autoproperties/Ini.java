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
package lodsve.core.autoproperties;

import lodsve.core.autoproperties.ini.IniLoader;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ini配置获取.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-31 14:19
 */
public class Ini {
    private static final Logger logger = LoggerFactory.getLogger(Ini.class);
    private static Map<String, Map<String, String>> inis;

    static {
        init();
    }

    private static void init() {
        try {
            inis = IniLoader.getIni();
        } catch (Exception e) {
            throw new RuntimeException("加载配置文件发生IO异常");
        }
    }

    /**
     * 获取keys
     *
     * @return
     */
    public static Set<String> getSections() {
        return inis.keySet();
    }

    /**
     * 根据section获取配置
     *
     * @param section
     * @return
     */
    public static Map<String, String> getInisBySection(String section) {
        return inis.get(section);
    }

    /**
     * 获取配置
     *
     * @param section
     * @param key
     * @return
     */
    public static String getIni(String section, String key) {
        Map<String, String> sections = inis.get(section);
        if (sections == null || sections.isEmpty()) {
            return StringUtils.EMPTY;
        }

        return sections.get(key);
    }

    /**
     * 获取布尔型配置
     *
     * @param section
     * @param key
     * @return
     */
    public static boolean getBoolean(String section, String key) {
        String config = getIni(section, key);

        return "true".equals(config) || "1".equals(config) || "y".equalsIgnoreCase(config) || "yes".equalsIgnoreCase(config);
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    public static Map<String, Map<String, String>> getInis() {
        return inis;
    }

    public static Map<String, Object> getInisProperties() {
        Map<String, Map<String, String>> all = getInis();
        Map<String, Object> result = new HashMap<>(16);
        for (String key : all.keySet()) {
            Map<String, String> subMap = all.get(key);
            for (String subKey : subMap.keySet()) {
                String newKey = key + "." + subKey;
                String value = subMap.get(subKey);

                result.put(newKey, value);
            }
        }

        return result;
    }

    /**
     * 获取指定ini资源
     *
     * @param resource
     * @return
     */
    public static Map<String, Map<String, String>> getFileIni(Resource resource) {
        Assert.notNull(resource, "资源不能为空！");

        Map<String, Map<String, String>> maps = new HashMap<>(16);
        try {
            maps.putAll(IniLoader.getIni(resource));
        } catch (Exception e) {
            logger.error("加载资源'{}'失败！", resource);
            logger.error(e.getMessage(), e);
        }

        return maps;
    }
}
