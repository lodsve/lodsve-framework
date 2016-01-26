package message.mongodb.core;

import message.base.utils.GenericUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * dynamic inject {@link MongoEntityInformation} into the repository which extends from {@link GenericMongoRepository}.<br/>
 * by the first generic info of the repository.means the domain class which has an annontation {@link org.springframework.data.mongodb.core.mapping.Document}.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-22 15:03
 */
@Component
public class MongoRepositoryBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Repository annotation = AnnotationUtils.findAnnotation(bean.getClass(), Repository.class);
        if (annotation == null) {
            return bean;
        }
        Class<?> supperClass = bean.getClass().getSuperclass();
        if (!GenericMongoRepository.class.equals(supperClass)) {
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
