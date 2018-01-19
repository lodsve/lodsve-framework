/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.core.context;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 非web应用spring的上下文(优先JavaConfig).
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:20
 */
class SpringContext {
    private SpringContext() {
    }

    static class Builder {
        private String configLocation = "application-context.xml";
        private Class<?> javaConfig;
        private Object bean;

        Builder config(String configLocation) {
            this.configLocation = configLocation;
            return this;
        }

        Builder config(Class<?> javaConfig) {
            this.javaConfig = javaConfig;
            return this;
        }

        Builder bean(Object bean) {
            this.bean = bean;
            return this;
        }

        void build() {
            ApplicationContext context;
            ConfigurableListableBeanFactory beanFactory;
            if (javaConfig != null && javaConfig.isAnnotationPresent(Configuration.class)) {
                context = new AnnotationConfigApplicationContext(javaConfig);
                beanFactory = ((AnnotationConfigApplicationContext) context).getBeanFactory();
            } else {
                context = new ClassPathXmlApplicationContext(configLocation);
                beanFactory = ((ClassPathXmlApplicationContext) context).getBeanFactory();
            }

            beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);

            ApplicationHelper.getInstance().addApplicationContext(context);
        }
    }
}
