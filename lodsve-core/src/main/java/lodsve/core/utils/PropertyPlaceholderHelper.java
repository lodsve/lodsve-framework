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

package lodsve.core.utils;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理字符串中的占位符.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 14-9-3 下午10:37
 */
public class PropertyPlaceholderHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderHelper.class);

    /**
     * 占位符前缀
     */
    private static final String PLACEHOLDER_PREFIX = "${";
    /**
     * 占位符后缀
     */
    private static final String PLACEHOLDER_SUFFIX = "}";
    /**
     * 左括号
     */
    private final static String LEFT_BRACE = "{";
    /**
     * 右括号
     */
    private final static String RIGTH_BRACE = "}";

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
    public static String replacePlaceholder(String text, boolean placeholderAsDefaultValue, Map<String, String> values) {
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

        List<String> placeholders = new ArrayList<>();
        int start = placeholderInFirst ? 0 : 1;
        for (int i = start; i < length; i++) {
            if (i < length) {
                String t = groups[i];
                //3.将所有的占位符取出
                if (StringUtils.contains(t, PLACEHOLDER_SUFFIX)) {
                    placeholders.add(StringUtils.substring(t, 0, StringUtils.indexOf(t, PLACEHOLDER_SUFFIX)));
                }
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

    /**
     * 格式化字符串，替换{0}{1}...，如果不成功，则默认返回原字符串
     *
     * @param formatString 需要格式化的字符串
     * @param args         参数
     * @return
     */
    public static String replaceNumholder(String formatString, Object... args) {
        String result = replaceNumholder(formatString, StringUtils.EMPTY, args);

        return StringUtils.isEmpty(result) ? formatString : result;
    }

    /**
     * 格式化字符串，替换{0}{1}...，如果不成功，则返回defaultVlaue
     *
     * @param formatString 需要格式化的字符串
     * @param defaultValue 如果不成功，返回的字符串
     * @param args         参数
     * @return
     */
    public static String replaceNumholder(String formatString, String defaultValue, Object... args) {
        if (StringUtils.isEmpty(formatString)) {
            logger.error("the formatString, args, defaultValue is required! please check given paramters are all right!");
            return StringUtils.EMPTY;
        }

        // 第一个'{'的位置
        int firstLeftBrace = formatString.indexOf(LEFT_BRACE);
        // 最后一个'}'的位置
        int lastRightBrace = formatString.lastIndexOf(RIGTH_BRACE);

        if (firstLeftBrace == -1 || lastRightBrace == -1) {
            return defaultValue == null ? formatString : defaultValue;
        }

        // 第一个'{'后面的那个数字
        String first = formatString.substring(firstLeftBrace + 1, firstLeftBrace + 2);
        // 最后一个'}'后面的那个数字
        String last = formatString.substring(lastRightBrace - 1, lastRightBrace);

        // 第一个序号和最后一个序号
        int firstSequence = Integer.parseInt(first);
        int lastSequence = Integer.parseInt(last);

        // {0}{1}...的个数与给定的值个数不一致，或者{0}{1}...不是按照这样递增的，那么返回错误,
        // 否则进行替换
        if (!((lastSequence - firstSequence + 1) < 0 || (lastSequence - firstSequence + 1) != args.length)) {
            // 替换规则：
            // 给定值的第一个值替换{0}，以此类推
            for (int j = 0; j < args.length; j++) {
                formatString = formatString.replace("{" + j + "}", args[j] + "");
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("foramtString has '{}' barces, but you given '{}' paramters!",
                        lastSequence - firstSequence + 1, args.length);
            }

            return defaultValue;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("format successed! the result is '{}'", formatString);
        }

        return formatString;
    }
}
