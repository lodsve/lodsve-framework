package message.datasource.annotations;

import message.datasource.core.factory.MongoDataSourceBeanDefinitionFactory;
import message.datasource.core.factory.RdbmsDataSourceBeanDefinitionFactory;
import message.datasource.core.factory.RedisDataSourceBeanDefinitionFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 通过注解的方式加载数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午7:20
 */
public class DataSourceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    public static final String DATASOURCE_NAME_ATTRIBUTE_NAME = "name";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean isRdbms = importingClassMetadata.hasAnnotation(Rdbms.class.getName());
        boolean isRedis = importingClassMetadata.hasAnnotation(Redis.class.getName());
        boolean isMongoDB = importingClassMetadata.hasAnnotation(MongoDB.class.getName());

        if (isRdbms) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(Rdbms.class.getName(), false));
            String name = attributes.getString(DATASOURCE_NAME_ATTRIBUTE_NAME);

            registry.registerBeanDefinition(name, new RdbmsDataSourceBeanDefinitionFactory(name).build());
        }

        if (isRedis) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(Redis.class.getName(), false));
            String name = attributes.getString(DATASOURCE_NAME_ATTRIBUTE_NAME);

            registry.registerBeanDefinition(name, new RedisDataSourceBeanDefinitionFactory(name).build());
        }

        if (isMongoDB) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MongoDB.class.getName(), false));
            String name = attributes.getString(DATASOURCE_NAME_ATTRIBUTE_NAME);

            registry.registerBeanDefinition(name, new MongoDataSourceBeanDefinitionFactory(name).build());
        }
    }
}
