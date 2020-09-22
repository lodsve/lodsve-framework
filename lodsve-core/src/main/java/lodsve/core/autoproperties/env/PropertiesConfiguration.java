/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
