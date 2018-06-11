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

import lodsve.core.properties.env.Configuration;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.env.PropertiesConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 系统配置文件.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-17 下午10:29
 */
public class Env {
    private static Configuration configuration;
    private static Properties env;

    static {
        init();
    }

    private static void init() {
        env = EnvLoader.getEnvs();
        configuration = new PropertiesConfiguration(env);
    }

    public static Configuration subset(String prefix) {
        return configuration.subset(prefix);
    }

    public static boolean containsKey(String key) {
        return configuration.containsKey(key);
    }

    public static Object getProperty(String key) {
        return configuration.getProperty(key);
    }

    public static Set<String> getKeys(String prefix) {
        return configuration.getKeys(prefix);
    }

    public static Set<String> getKeys() {
        return configuration.getKeys();
    }

    public static Properties getProperties(String key) {
        return configuration.getProperties(key);
    }

    public static boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return configuration.getBoolean(key, defaultValue);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return configuration.getBoolean(key, defaultValue);
    }

    public static byte getByte(String key) {
        return configuration.getByte(key);
    }

    public static byte getByte(String key, byte defaultValue) {
        return configuration.getByte(key, defaultValue);
    }

    public static Byte getByte(String key, Byte defaultValue) {
        return configuration.getByte(key, defaultValue);
    }

    public static double getDouble(String key) {
        return configuration.getDouble(key);
    }

    public static double getDouble(String key, double defaultValue) {
        return configuration.getDouble(key, defaultValue);
    }

    public static Double getDouble(String key, Double defaultValue) {
        return configuration.getDouble(key, defaultValue);
    }

    public static float getFloat(String key) {
        return configuration.getFloat(key);
    }

    public static float getFloat(String key, float defaultValue) {
        return configuration.getFloat(key, defaultValue);
    }

    public static Float getFloat(String key, Float defaultValue) {
        return configuration.getFloat(key, defaultValue);
    }

    public static int getInt(String key) {
        return configuration.getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return configuration.getInt(key, defaultValue);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return configuration.getInteger(key, defaultValue);
    }

    public static long getLong(String key) {
        return configuration.getLong(key);
    }

    public static long getLong(String key, long defaultValue) {
        return configuration.getLong(key, defaultValue);
    }

    public static Long getLong(String key, Long defaultValue) {
        return configuration.getLong(key, defaultValue);
    }

    public static short getShort(String key) {
        return configuration.getShort(key);
    }

    public static short getShort(String key, short defaultValue) {
        return configuration.getShort(key, defaultValue);
    }

    public static Short getShort(String key, Short defaultValue) {
        return configuration.getShort(key, defaultValue);
    }

    public static BigDecimal getBigDecimal(String key) {
        return configuration.getBigDecimal(key);
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return configuration.getBigDecimal(key, defaultValue);
    }

    public static BigInteger getBigInteger(String key) {
        return configuration.getBigInteger(key);
    }

    public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return configuration.getBigInteger(key, defaultValue);
    }

    public static String getString(String key) {
        return configuration.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        return configuration.getString(key, defaultValue);
    }

    public static String[] getStringArray(String key) {
        return configuration.getStringArray(key);
    }

    public static List<Object> getList(String key) {
        return configuration.getList(key);
    }

    public static List<Object> getList(String key, List<?> defaultValue) {
        return configuration.getList(key, defaultValue);
    }

    public static Resource getFileEnv(String fileName) {
        return EnvLoader.getFileEnv(fileName);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getEnvs() {
        return new HashMap<>((Map) env);
    }

    public static Map<String, String> getSystemEnvs() {
        Map<String, String> env = System.getenv();
        Properties properties = System.getProperties();

        Map<String, String> globalProperties = new HashMap<>(env.size() + properties.size());

        globalProperties.putAll(env);
        for (Object key : properties.keySet()) {
            globalProperties.put(key.toString(), properties.getProperty(key.toString()));
        }

        return globalProperties;
    }

    public static Configuration getFrameworkEnv(String fileName) {
        Assert.hasText(fileName, "文件名不能为空！");

        Resource resource = EnvLoader.getFrameworkEnv(fileName);

        return new PropertiesConfiguration(EnvLoader.getFileEnvs(resource));
    }
}
