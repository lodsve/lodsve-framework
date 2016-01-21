package message.datasource.core.factory;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午6:15
 */
public class MongoDataSourceBeanDefinitionFactory {
    private String dataSourceName;

    public MongoDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public BeanDefinition build() {
        return null;
    }
}
