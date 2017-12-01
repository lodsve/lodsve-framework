package lodsve.redis.core.connection;

import lodsve.core.autoconfigure.AutoConfigurationBuilder;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.redis.core.config.RedisProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * redis数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:10
 */
public class RedisDataSourceBeanDefinitionFactory {
    private String dataSourceName;
    private RedisProperties redisProperties;

    public RedisDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        AutoConfigurationBuilder.Builder<RedisProperties> builder = new AutoConfigurationBuilder.Builder<>();
        builder.setAnnotation(RedisProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(RedisProperties.class);

        this.redisProperties = builder.build();
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LodsveRedisConnectionFactory.class);

        beanDefinitionBuilder.addConstructorArgValue(dataSourceName);
        beanDefinitionBuilder.addConstructorArgValue(redisProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }
}
