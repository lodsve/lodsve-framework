package lodsve.core.relaxedbind;

import lodsve.core.properties.env.Configuration;
import lodsve.core.properties.env.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
