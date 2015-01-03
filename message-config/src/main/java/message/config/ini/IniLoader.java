package message.config.ini;

import static message.config.core.InitConfigPath.PARAMS_ROOT;

import message.utils.FileUtils;
import message.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static void load(Map<String, Map<String, String>> values, Resource resource) throws Exception {
        if (resource == null)
            return;

        List<String> lines = FileUtils.readLines(resource.getInputStream());

        String section = "";
        for(String line : lines){
            if(isBlankLine(line)) {
                continue;
            }

            if(isCommentLine(line)) {
                continue;
            }

            if(isSectionLine(line)) {
                section = line.substring(1, line.length() - 1);
                continue;
            }

            Map<String, String> sectionValue = values.get(section);
            if(sectionValue == null) {
                sectionValue = new HashMap<String, String>();
            }

            String[] temp = StringUtils.split(line, "=");
            if(temp == null || temp.length != 2) {
                continue;
            }
            String key = temp[0];
            String value = temp[1];

            sectionValue.put(key, value);
            values.put(section, sectionValue);
        }
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
    protected static boolean isSectionLine(String line) {
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
    protected static boolean isBlankLine(String line) {
        return StringUtils.isEmpty(line);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + PARAMS_ROOT + "/*.ini");

        for (Resource res : resources) {
            load(values, res);
        }
    }
}
