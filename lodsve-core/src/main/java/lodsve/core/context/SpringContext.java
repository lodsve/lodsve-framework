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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/20 上午10:20
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
