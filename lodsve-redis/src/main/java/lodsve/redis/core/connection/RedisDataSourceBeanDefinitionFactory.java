package lodsve.redis.core.connection;

import lodsve.base.config.auto.AutoConfigurationCreator;
import lodsve.base.config.auto.annotations.ConfigurationProperties;
import lodsve.redis.core.config.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * redis数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:10
 */
public class RedisDataSourceBeanDefinitionFactory {
    public static final Logger logger = LoggerFactory.getLogger(RedisDataSourceBeanDefinitionFactory.class);
    private String dataSourceName;
    private RedisProperties redisProperties;

    public RedisDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        AutoConfigurationCreator.Builder<RedisProperties> builder = new AutoConfigurationCreator.Builder<>();
        builder.setAnnotation(RedisProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(RedisProperties.class);

        try {
            this.redisProperties = builder.build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            this.redisProperties = new RedisProperties();
        }
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LodsveRedisConnectionFactory.class);

        beanDefinitionBuilder.addConstructorArgValue(dataSourceName);
        beanDefinitionBuilder.addConstructorArgValue(redisProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }
}
