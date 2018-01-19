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

package lodsve.core.properties.ini;

import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.properties.ParamsHome;
import lodsve.core.utils.FileUtils;
import lodsve.core.utils.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 解析ini文件.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2014-12-31 14:25
 */
public class IniLoader {

    private static Map<String, Map<String, String>> values = new HashMap<>();

    /**
     * The characters that signal the start of a comment line.
     */
    private static final String COMMENT_CHARS = "#;";

    public static Map<String, Map<String, String>> getIni() {
        return values;
    }

    public static Map<String, Map<String, String>> getIni(Resource resource) {
        Map<String, Map<String, String>> maps = new HashMap<>(16);
        load(maps, resource);

        return maps;
    }

    private static void load(Map<String, Map<String, String>> values, Resource resource) {
        if (resource == null) {
            return;
        }

        List<String> lines;
        try {
            lines = FileUtils.readLines(resource.getInputStream());
        } catch (Exception e) {
            return;
        }

        String section = "";
        List<String> lineArrays = new ArrayList<>();
        for (String line : lines) {
            if (isBlankLine(line)) {
                continue;
            }

            if (isCommentLine(line)) {
                continue;
            }

            if (isSectionLine(line)) {
                putInValues(values, section, convertLinesToProperties(StringUtils.join(lineArrays, "\r\n")));
                lineArrays.clear();

                section = line.substring(1, line.length() - 1);
                continue;
            }

            lineArrays.add(line);
        }

        //最后一个section
        putInValues(values, section, convertLinesToProperties(StringUtils.join(lineArrays, "\r\n")));
    }

    /**
     * Determine if the given line is a comment line.
     *
     * @param line The line to check.
     * @return true if the line is empty or starts with one of the comment
     * characters
     */
    private static boolean isCommentLine(String line) {
        if (line == null) {
            return false;
        }
        // blank lines are also treated as comment lines
        return line.length() < 1 || COMMENT_CHARS.indexOf(line.charAt(0)) >= 0;
    }

    /**
     * Determine if the given line is a section.
     *
     * @param line The line to check.
     * @return true if the line contains a section
     */
    private static boolean isSectionLine(String line) {
        if (line == null) {
            return false;
        }
        return line.startsWith("[") && line.endsWith("]");
    }

    /**
     * Determine if the given line is blank line.
     *
     * @param line The line to check.
     * @return true if the line is blank line.
     */
    private static boolean isBlankLine(String line) {
        return StringUtils.isEmpty(line);
    }

    /**
     * 将多行类似Properties文件格式的字符串转换成Properties对象
     *
     * @param lines 多行，以回车换行隔开(\r\n)
     * @return
     */
    private static Properties convertLinesToProperties(String lines) {
        Properties prop = new Properties();
        if (StringUtils.isEmpty(lines)) {
            return prop;
        }

        try {
            InputStream is = new ByteArrayInputStream(lines.getBytes());
            prop.load(is);
        } catch (IOException e) {
            return prop;
        }

        return prop;
    }

    private static void putInValues(Map<String, Map<String, String>> values, String section, Properties prop) {
        if (StringUtils.isEmpty(section)) {
            return;
        }

        Map<String, String> sectionValues = values.get(section);

        if (sectionValues == null) {
            sectionValues = new HashMap<>(prop.size());
        }

        Set<?> keys = prop.keySet();
        for (Object key : keys) {
            sectionValues.put(key.toString(), prop.getProperty(key.toString()));
        }

        values.put(section, sectionValues);
    }

    public static void init() {
        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/*.ini");
        } catch (IOException e) {
            return;
        }

        for (Resource res : resources) {
            load(values, res);
        }
    }

}
