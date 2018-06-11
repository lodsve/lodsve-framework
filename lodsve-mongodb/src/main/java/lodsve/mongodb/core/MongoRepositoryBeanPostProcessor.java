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
