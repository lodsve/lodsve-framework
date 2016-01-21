package message.datasource.core.factory;

import message.config.SystemConfig;
import message.config.auto.AutoConfigurationCreator;
import message.config.auto.annotations.ConfigurationProperties;
import message.datasource.config.RabbitProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * rabbit mq 数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午6:20
 */
public class RabbitDataSourceBeanDefinitionFactory {
    private static final String DATASOURCE_FILE_NAME = "rabbit.properties";

    private String dataSourceName;
    private RabbitProperties rabbitProperties;

    public RabbitDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        try {
            this.rabbitProperties = new AutoConfigurationCreator(SystemConfig.getFileConfiguration(DATASOURCE_FILE_NAME)).createBean(RabbitProperties.class, RabbitProperties.class.getAnnotation(ConfigurationProperties.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CachingConnectionFactory.class);

        RabbitProperties.RabbitConnection connection = rabbitProperties.getProject().get(dataSourceName);
        if (connection == null) {
            return null;
        }

        beanDefinitionBuilder.addPropertyValue("address", connection.getAddress());
        beanDefinitionBuilder.addPropertyValue("username", connection.getUsername());
        beanDefinitionBuilder.addPropertyValue("password", connection.getPassword());

        return beanDefinitionBuilder.getBeanDefinition();
    }
}
