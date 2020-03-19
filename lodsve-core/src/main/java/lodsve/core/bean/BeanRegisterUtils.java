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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-11-0011 11:29
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
