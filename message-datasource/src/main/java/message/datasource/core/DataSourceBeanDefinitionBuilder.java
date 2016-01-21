package message.datasource.core;

import message.datasource.annotations.DataSourceType;
import message.datasource.core.factory.MongoDataSourceBeanDefinitionFactory;
import message.datasource.core.factory.RdbmsDataSourceBeanDefinitionFactory;
import message.datasource.core.factory.RedisDataSourceBeanDefinitionFactory;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 注册数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午7:24
 */
public class DataSourceBeanDefinitionBuilder {
    private String dataSourceName;
    private DataSourceType type;

    public DataSourceBeanDefinitionBuilder(String dataSourceName, DataSourceType type) {
        this.dataSourceName = dataSourceName;
        this.type = type;
    }

    public BeanDefinition build() {
        BeanDefinition beanDefinition;
        switch (type) {
            case RDBMS:
                beanDefinition = new RdbmsDataSourceBeanDefinitionFactory(dataSourceName).build();
                break;
            case REDIS:
                beanDefinition = new RedisDataSourceBeanDefinitionFactory(dataSourceName).build();
                break;
            case MONGO:
                beanDefinition = new MongoDataSourceBeanDefinitionFactory(dataSourceName).build();
                break;
            default:
                beanDefinition = new RdbmsDataSourceBeanDefinitionFactory(dataSourceName).build();
                break;
        }

        return beanDefinition;
    }
}
