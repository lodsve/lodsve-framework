package lodsve.mongodb.config;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.mongodb.core.MongoRepositoryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

/**
 * 基本配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午10:15
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@EnableAspectJAutoProxy
@ComponentScan("lodsve.mongodb.connection")
public class MongoConfiguration {
    @Bean
    public DefaultMongoTypeMapper defaultMongoTypeMapper() {
        return new DefaultMongoTypeMapper();
    }

    @Bean
    public MongoRepositoryBeanPostProcessor beanPostProcessor() {
        return new MongoRepositoryBeanPostProcessor();
    }
}
