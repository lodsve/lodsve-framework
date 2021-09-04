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
