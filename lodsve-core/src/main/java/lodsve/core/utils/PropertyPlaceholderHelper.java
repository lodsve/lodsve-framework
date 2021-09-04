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
package lodsve.core.utils;

import lodsve.core.autoproperties.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.Map;

/**
 * 处理字符串中的占位符.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-9-3 下午10:37
 */
public class PropertyPlaceholderHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderHelper.class);
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
     * @param placeholderAsDefaultValue 如果一个占位符找不到值，是否使用占位符作为值
     * @return 替换后的字符串
     * @pa ram text                      要替换的文本(包含${...})
     */
    public static String replacePlaceholder(String text, boolean placeholderAsDefaultValue) {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(new EnvPropertySource("envs", Env.getEnvs()));
        propertySources.addLast(new EnvPropertySource("system", Env.getSystemEnvs()));

        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);

        if (!placeholderAsDefaultValue) {
            return resolver.resolveRequiredPlaceholders(text);
        } else {
            return resolver.resolvePlaceholders(text);
        }
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

    private static class EnvPropertySource extends EnumerablePropertySource<Map<String, String>> {
        private EnvPropertySource(String name, Map<String, String> source) {
            super(name, source);
        }


        @Override
        public Object getProperty(String name) {
            return this.source.get(name);
        }

        @Override
        public boolean containsProperty(String name) {
            return this.source.containsKey(name);
        }

        @Override
        public String[] getPropertyNames() {
            return org.springframework.util.StringUtils.toStringArray(this.source.keySet());
        }
    }
}
