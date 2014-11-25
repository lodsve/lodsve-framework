package message.utils;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 处理字符串中的占位符.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-3 下午10:37
 */
public class PropertyPlaceholderHelper {
    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    /**
     * 私有化构造器，外部不可以实例化
     */
    private PropertyPlaceholderHelper() {

    }

    /**
     * 替换text中的占位符<br/>
     * eg:<br/>
     * 1.text值为:<br/>
     * ${hi}，我是${user.name}，英文名是${user.eglish.name}!<br/><br/>
     * 2.values值为:<br/>
     * <code>
     * Map<String, String> params = new HashMap<String, String>();<br/>
     * params.put("user.name", "孙昊");<br/>
     * params.put("user.eglish.name", "Hello World");<br/>
     * params.put("hi", "你好");<br/><br/>
     * </code>
     * 那么结果将是:<br/>
     * 你好，我是孙昊，英文名是Hello World!
     *
     * @param text                      要替换的文本(包含${...})
     * @param placeholderAsDefaultValue 如果一个占位符找不到值，是否使用占位符作为值
     * @param values                    值的map集合
     * @return
     */
    public static String processProperties(String text, boolean placeholderAsDefaultValue, Map<String, String> values) {
        Assert.hasText(text, "source text can't be null!");
        if (!needReplace(text)) {
            return text;
        }
        Assert.notEmpty(values, "values can't be null!");

        //1.根据前缀分组
        String[] groups = StringUtils.split(text, PLACEHOLDER_PREFIX);

        //2.遍历分组，如果有后缀，则取出
        int length = groups.length;

        //占位符在第一位
        boolean placeholderInFirst = StringUtils.startsWith(text, PLACEHOLDER_PREFIX);
        Assert.isTrue(placeholderInFirst || length > 1, "given text has no placeholder!");

        List<String> placeholders = new ArrayList<String>();
        int start = placeholderInFirst ? 0 : 1;
        for (int i = start; i < length; i++) {
            if (i < length) {
                String t = groups[i];
                //3.将所有的占位符取出
                if (StringUtils.contains(t, PLACEHOLDER_SUFFIX))
                    placeholders.add(StringUtils.substring(t, 0, StringUtils.indexOf(t, PLACEHOLDER_SUFFIX)));
            }
        }

        //4.进行替换
        String result = text;
        for (String ph : placeholders) {
            String placeholder = PLACEHOLDER_PREFIX + ph + PLACEHOLDER_SUFFIX;
            String value = values.get(ph);
            if (StringUtils.isEmpty(value) && !placeholderAsDefaultValue) {
                throw new NestableRuntimeException("process text '{" + text + "}' error! placeholder is '{" + placeholder + "}'!");
            }

            value = (StringUtils.isNotEmpty(value) ? value : ph);

            //5.替换text
            result = StringUtils.replace(result, placeholder, value);
        }

        return result;
    }

    /**
     * 判断是否需要进行替换
     *
     * @param text 待检查文字
     * @return
     */
    private static boolean needReplace(String text) {
        return StringUtils.hasExcerpt(text, "\\$\\{.*?\\}") > 0;
    }
}
