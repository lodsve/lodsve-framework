package lodsve.core.bean;

import lodsve.core.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;
import java.util.Set;

/**
 * 动态注册Bean.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-11-0011 11:29
 */
public class BeanRegisterUtils {
    private BeanRegisterUtils() {
    }

    /**
     * 注册
     *
     * @param beanDefinitions BeanDefinition的map集合（key为beanName， value为BeanDefinition）
     * @param registry        spring注册Bean的对象
     */
    public static void registerBeans(Map<String, BeanDefinition> beanDefinitions, BeanDefinitionRegistry registry) {
        registerBeans(beanDefinitions, registry, new DefaultBeanNameBuilder());
    }

    /**
     * 注册，可以对beanName进行修改
     *
     * @param beanDefinitions BeanDefinition的map集合（key为beanName， value为BeanDefinition）
     * @param registry        spring注册Bean的对象
     */
    public static void registerBeans(Map<String, BeanDefinition> beanDefinitions, BeanDefinitionRegistry registry, BeanNameBuilder builder) {
        if (MapUtils.isEmpty(beanDefinitions)) {
            return;
        }

        if (registry == null) {
            return;
        }

        Set<String> beanNames = beanDefinitions.keySet();
        for (String beanName : beanNames) {
            BeanDefinition bean = beanDefinitions.get(beanName);
            beanName = builder.buildBeanName(beanName, bean);
            if (registry.containsBeanDefinition(beanName)) {
                continue;
            }

            registry.registerBeanDefinition(beanName, bean);
        }
    }

    /**
     * beanName builder
     */
    public interface BeanNameBuilder {
        /**
         * 设置beanName
         *
         * @param key   map中的key
         * @param value BeanDefinition
         * @return
         */
        String buildBeanName(String key, BeanDefinition value);
    }

    /**
     * 默认生成器
     */
    public static class DefaultBeanNameBuilder implements BeanNameBuilder {

        @Override
        public String buildBeanName(String key, BeanDefinition value) {
            return StringUtils.isNotBlank(key) ? key : (value != null ? value.getBeanClassName() : "BeanName#" + Thread.currentThread().getId());
        }
    }
}
