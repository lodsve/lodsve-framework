package message.redis.core.connection;

import message.config.SystemConfig;
import message.config.auto.AutoConfigurationCreator;
import message.config.auto.annotations.ConfigurationProperties;
import message.redis.core.config.RedisProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * redis数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:10
 */
public class RedisDataSourceBeanDefinitionFactory {
    private String dataSourceName;
    private RedisProperties redisProperties;
    private static final String DATASOURCE_FILE_NAME = "redis.properties";

    public RedisDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        try {
            AutoConfigurationCreator.Builder<RedisProperties> builder = new AutoConfigurationCreator.Builder<>();
            builder.setAnnotation(RedisProperties.class.getAnnotation(ConfigurationProperties.class));
            builder.setClazz(RedisProperties.class);
            Properties prop = new Properties();
            PropertiesLoaderUtils.fillProperties(prop, SystemConfig.getConfigFile(DATASOURCE_FILE_NAME));
            builder.setProp(prop);

            this.redisProperties = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CosmosRedisConnectionFactory.class);

        beanDefinitionBuilder.addConstructorArgValue(dataSourceName);
        beanDefinitionBuilder.addConstructorArgValue(redisProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }
}
