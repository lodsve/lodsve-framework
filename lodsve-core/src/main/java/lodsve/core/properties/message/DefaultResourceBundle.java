/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.core.properties.message;

import java.util.*;

/**
 * 扩展的资源绑定.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-4-17 下午10:44
 */
public class DefaultResourceBundle extends ResourceBundle {

    private Map<String, Object> defaultBundleMap;
    private Map<Locale, Map<String, Object>> bundleMap;
    private Locale locale;

    public DefaultResourceBundle(Map<String, Object> defaultBundleMap, Map<Locale, Map<String, Object>> bundleMap, Locale locale) {
        this.defaultBundleMap = defaultBundleMap;
        this.bundleMap = bundleMap;
        this.locale = locale;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result;
        if (locale == null) {
            //locale为空,则取中文环境
            result = this.defaultBundleMap.get(key);
        } else {
            //从具体语言中取值
            Map<String, Object> localeMap = this.bundleMap.get(locale);
            if (localeMap == null) {
                localeMap = this.defaultBundleMap;
            }

            result = localeMap != null ? localeMap.get(key) : null;

            //取不到值,则值默认为key
            if (result == null) {
                result = key;
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getKeys() {
        Set<String> keys = this.defaultBundleMap.keySet();

        return Collections.enumeration(keys);
    }
}
