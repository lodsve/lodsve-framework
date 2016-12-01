package lodsve.mongodb.core;

import com.mongodb.MongoURI;
import lodsve.core.config.autoconfigure.AutoConfigurationCreator;
import lodsve.core.config.annotations.ConfigurationProperties;
import lodsve.mongodb.config.MongoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

/**
 * mongo db datasource.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午6:15
 */
public class MongoDataSourceBeanDefinitionFactory {
    public static final Logger logger = LoggerFactory.getLogger(MongoDataSourceBeanDefinitionFactory.class);
    private static final String URL_PREFIX = "mongodb://";

    private String dataSourceName;
    private MongoProperties mongoProperties;

    public MongoDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        AutoConfigurationCreator.Builder<MongoProperties> builder = new AutoConfigurationCreator.Builder<>();
        builder.setAnnotation(MongoProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(MongoProperties.class);

        try {
            this.mongoProperties = builder.build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            this.mongoProperties = new MongoProperties();
        }
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder mongoURIBean = BeanDefinitionBuilder.genericBeanDefinition(MongoURI.class);
        mongoURIBean.addConstructorArgValue(getMongoUri());

        BeanDefinitionBuilder mongoDbFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SimpleMongoDbFactory.class);
        mongoDbFactoryBean.addConstructorArgValue(mongoURIBean.getBeanDefinition());

        return mongoDbFactoryBean.getBeanDefinition();
    }

    private String getMongoUri() {
        MongoProperties.MongoConnection connection = mongoProperties.getProject().get(dataSourceName);

        String url = connection.getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("uri must not null");
        }
        if (!url.startsWith(URL_PREFIX)) {
            throw new IllegalArgumentException("uri needs to start with " + URL_PREFIX);
        }

        StringBuilder uriBuilder = new StringBuilder(URL_PREFIX);


        String username = connection.getUsername();
        String password = connection.getPassword();

        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            uriBuilder.append(username + ":" + password + "@");
        }

        uriBuilder.append(url.substring(URL_PREFIX.length()));

        uriBuilder.append("?maxpoolsize=");
        if (connection.getMaxpoolsize() != 0) {
            uriBuilder.append(connection.getMaxpoolsize());
        } else {
            uriBuilder.append(mongoProperties.getMaxpoolsize());
        }

        return uriBuilder.toString();
    }
}
