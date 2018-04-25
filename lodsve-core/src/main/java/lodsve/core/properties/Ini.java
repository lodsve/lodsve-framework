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

import lodsve.core.properties.ini.IniLoader;
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
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2014-12-31 14:19
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

    public static Map<String, String> getInisProperties() {
        Map<String, Map<String, String>> all = getInis();
        Map<String, String> result = new HashMap<>(16);
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
