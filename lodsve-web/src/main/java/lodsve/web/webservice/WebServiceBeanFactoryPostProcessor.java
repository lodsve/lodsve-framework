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
package lodsve.web.webservice;

import lodsve.core.utils.StringUtils;
import org.apache.cxf.configuration.spring.AbstractBeanDefinitionParser;
import org.apache.cxf.configuration.spring.BusWiringType;
import org.apache.cxf.jaxws.spring.EndpointDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

import javax.jws.WebService;

/**
 * WebService Bean FactoryPostProcessor.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-15-0015 09:37
 */
public class WebServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceBeanFactoryPostProcessor.class);
    private static final String WIRE_BUS_ATTRIBUTE = AbstractBeanDefinitionParser.class.getName() + ".wireBus";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return;
        }

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String className = beanDefinition.getBeanClassName();
            if (StringUtils.isBlank(className)) {
                continue;
            }
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
                continue;
            }

            if (!clazz.isAnnotationPresent(WebService.class)) {
                continue;
            }

            String address;
            if (!clazz.isAnnotationPresent(AddressProvider.class)) {
                address = "/" + beanName;
            } else {
                AddressProvider provider = clazz.getAnnotation(AddressProvider.class);
                address = provider.value();
            }

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(EndpointDefinitionParser.SpringEndpointImpl.class);
            builder.addPropertyValue("address", wrapAddress(address));
            builder.addPropertyValue("checkBlockConstruct", Boolean.TRUE);
            builder.getRawBeanDefinition().setAttribute(WIRE_BUS_ATTRIBUTE, BusWiringType.CONSTRUCTOR);
            builder.setInitMethodName("publish");
            builder.setDestroyMethodName("stop");
            builder.setLazyInit(false);
            builder.addConstructorArgReference(beanName);

            registerBeanDefinition((DefaultListableBeanFactory) beanFactory, beanName, builder.getBeanDefinition());
        }
    }

    private String wrapAddress(String address) {
        if (StringUtils.isBlank(address)) {
            address = "/";
        }

        if (!StringUtils.startsWith(address, "/")) {
            address = "/" + address;
        }

        return address;
    }

    private void registerBeanDefinition(DefaultListableBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(generateBeanName(beanName, beanFactory), beanDefinition);
    }

    private String generateBeanName(String name, DefaultListableBeanFactory beanFactory) {
        if (beanFactory.containsBeanDefinition(name)) {
            name += "#endpoint";
        }

        return name;
    }
}
