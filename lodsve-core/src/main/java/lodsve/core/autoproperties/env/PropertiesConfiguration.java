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
package lodsve.core.autoproperties.env;

import java.util.*;

/**
 * 配置文件.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-17 下午9:44
 */
public class PropertiesConfiguration extends AbstractConfiguration {

    private final Map<String, Object> store = new HashMap<String, Object>();

    public PropertiesConfiguration(Properties properties) {
        load(properties);
    }

    private void load(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            addPropertyValues((String) entry.getKey(), entry.getValue(),
                isDelimiterParsingDisabled() ? DISABLED_DELIMITER : getListDelimiter());
        }
    }

    @Override
    public Set<String> getKeys() {
        return store.keySet();
    }

    @Override
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
        return store.get(key);
    }

    protected void addPropertyValues(String key, Object value, char delimiter) {
        Iterator<?> it = PropertyConverter.toIterator(value, delimiter);
        while (it.hasNext()) {
            addPropertyDirect(key, it.next());
        }
    }

    protected void addPropertyDirect(String key, Object value) {
        Object previousValue = getProperty(key);

        if (previousValue == null) {
            store.put(key, value);
        } else if (previousValue instanceof List) {
            // safe to case because we have created the lists ourselves
            @SuppressWarnings("unchecked")
            List<Object> valueList = (List<Object>) previousValue;
            // the value is added to the existing list
            valueList.add(value);
        } else {
            // the previous value is replaced by a list containing the previous
            // value and the new value
            List<Object> list = new ArrayList<Object>();
            list.add(previousValue);
            list.add(value);

            store.put(key, list);
        }
    }

    @Override
    public Map<String, Object> getStore() {
        return store;
    }
}
