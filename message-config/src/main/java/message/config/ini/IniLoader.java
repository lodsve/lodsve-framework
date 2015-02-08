package message.config.ini;

import message.config.core.InitConfigPath;

import message.utils.FileUtils;
import message.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 解析ini文件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-31 14:25
 */
public class IniLoader implements InitializingBean {

    private static Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();

    /**
     * The characters that signal the start of a comment line.
     */
    private static final String COMMENT_CHARS = "#;";

    public static Map<String, Map<String, String>> getIni() throws Exception {
        return values;
    }

    public static Map<String, Map<String, String>> getIni(Resource resource) throws Exception {
        Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
        load(maps, resource);

        return maps;
    }

    private static void load(Map<String, Map<String, String>> values, Resource resource) throws Exception {
        if (resource == null)
            return;

        List<String> lines = FileUtils.readLines(resource.getInputStream());

        String section = "";
        List<String> lineArrays = new ArrayList<String>();
        for(String line : lines){
            if(isBlankLine(line)) {
                continue;
            }

            if(isCommentLine(line)) {
                continue;
            }

            if(isSectionLine(line)) {
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
    private static Properties convertLinesToProperties(String lines){
        Properties prop = new Properties();
        if(StringUtils.isEmpty(lines)) {
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

    private static void putInValues(Map<String, Map<String, String>> values, String section, Properties prop){
        if(StringUtils.isEmpty(section)) {
            return;
        }

        Map<String, String> sectionValues = values.get(section);

        if(sectionValues == null)
            sectionValues = new HashMap<String, String>();

        Set<?> keys = prop.keySet();
        for(Object key : keys){
            sectionValues.put(key.toString(), prop.getProperty(key.toString()));
        }

        values.put(section, sectionValues);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + InitConfigPath.getParamsRoot() + "/*.ini");

        for (Resource res : resources) {
            load(values, res);
        }
    }

}
