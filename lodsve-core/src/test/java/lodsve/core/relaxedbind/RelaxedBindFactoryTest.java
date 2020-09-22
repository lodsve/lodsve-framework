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
package lodsve.core.relaxedbind;

import lodsve.core.autoproperties.env.Configuration;
import lodsve.core.autoproperties.env.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/18 11:20
 */
public class RelaxedBindFactoryTest {
    @Test
    public void test1() {
        BeanWrapper beanWrapper = new BeanWrapperImpl(Demo.class);
        PropertyDescriptor descriptor = beanWrapper.getPropertyDescriptor("args");

        Class<?> type = descriptor.getPropertyType();
        Assert.assertTrue(type.isArray());
        Assert.assertEquals(String.class, type.getComponentType());
    }

    @Test
    public void test2() {
        Properties properties = new Properties();
        properties.put("lodsve.demo.args.0", "1");
        properties.put("lodsve.demo.args.1", "2");
        properties.put("lodsve.demo.args.2", "3");
        properties.put("lodsve.demo.args.3", "4");

        String prefix = "lodsve.demo.args";
        PropertiesConfiguration configuration = new PropertiesConfiguration(properties);
        Configuration subset = configuration.subset(prefix);

        Demo demo = new Demo();
        BeanWrapper beanWrapper = new BeanWrapperImpl(demo);

        Set<String> keys = subset.getKeys();
        List<Object> list = new ArrayList<>(keys.size());
        int size = keys.size();
        for (int i = 0; i < size; i++) {
            String key = prefix + "." + i;
            Object value = configuration.getProperty(key);

            list.add(value);
        }

        beanWrapper.setPropertyValue("args", list.toArray());

        Assert.assertArrayEquals(new String[]{"1", "2", "3", "4"}, demo.getArgs());
    }

    private static class Demo {
        private String[] args;

        public String[] getArgs() {
            return args;
        }

        public void setArgs(String[] args) {
            this.args = args;
        }

        @Override
        public String toString() {
            return "args = " + Arrays.toString(args);
        }
    }
}
