package lodsve.redis.core.connection;

import lodsve.core.autoconfigure.AutoConfigurationBuilder;
import lodsve.redis.core.properties.RedisProperties;
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

        this.redisProperties = new AutoConfigurationBuilder.Builder<>(RedisProperties.class).build();
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LodsveRedisConnectionFactory.class);

        beanDefinitionBuilder.addConstructorArgValue(dataSourceName);
        beanDefinitionBuilder.addConstructorArgValue(redisProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }
}
