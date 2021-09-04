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
package lodsve.core.autoproperties.ini;

import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-31 14:25
 */
public class IniLoader {

    private static final Map<String, Map<String, String>> values = new HashMap<>();

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
