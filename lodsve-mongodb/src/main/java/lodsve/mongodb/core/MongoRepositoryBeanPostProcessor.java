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
package lodsve.mongodb.core;

import lodsve.core.utils.GenericUtils;
import lodsve.mongodb.repository.LodsveMongoRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

/**
 * dynamic inject {@link MongoEntityInformation} into the repository which extends from {@link LodsveMongoRepository}.<br/>
 * by the first generic info of the repository.means the domain class which has an annontation {@link org.springframework.data.mongodb.core.mapping.Document}.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-1-22 15:03
 */
public class MongoRepositoryBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Repository annotation = AnnotationUtils.findAnnotation(bean.getClass(), Repository.class);
        if (annotation == null) {
            return bean;
        }
        Class<?> supperClass = bean.getClass().getSuperclass();
        if (!LodsveMongoRepository.class.equals(supperClass)) {
            return bean;
        }

        Class<?> domainClass = GenericUtils.getGenericParameter0(bean.getClass());
        MongoRepositoryFactory repositoryFactory = context.getBean(MongoRepositoryFactory.class);
        MongoEntityInformation information = repositoryFactory.getEntityInformation(domainClass);

        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
        beanWrapper.setPropertyValue("entityInformation", information);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
