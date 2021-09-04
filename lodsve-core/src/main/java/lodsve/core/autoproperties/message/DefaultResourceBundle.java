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
package lodsve.core.autoproperties.message;

import java.util.*;

/**
 * 扩展的资源绑定.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-4-17 下午10:44
 */
public class DefaultResourceBundle extends ResourceBundle {

    private final Map<String, Object> defaultBundleMap;
    private final Map<Locale, Map<String, Object>> bundleMap;
    private final Locale locale;

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
