package message.mybatis.configs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import message.config.SystemConfig;
import message.config.loader.properties.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * 关系型数据库数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:00
 */
public class RdbmsDataSourceBeanDefinitionFactory {
    /**
     * 数据源配置的key
     */
    private static final String DATASOURCE_CLASS = "datasources.rdbms.dataSourceClass";
    private static final String DATASOURCE_PROPERTY_PREFIX = "datasources.rdbms";
    private static final String DEFAULT_PROPERTIES_KEY_PREFIX = DATASOURCE_PROPERTY_PREFIX + ".default";
    private static final String DATASOURCE_FILE_NAME = "rdbms.properties";

    private Configuration configuration;
    private String dataSourceName;

    public RdbmsDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.configuration = SystemConfig.getFileConfiguration(DATASOURCE_FILE_NAME);
        this.dataSourceName = dataSourceName;
    }

    public BeanDefinition build() {
        String dataSourceClassName = configuration.getString(DATASOURCE_CLASS);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        Map<String, String> properties = this.getProperties(dataSourceName);
        this.setDataSourceProperty(beanDefinitionBuilder, properties);

        this.setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Map<String, String> getProperties(String dataSourceName) {
        Map<String, String> properties = new HashMap<>();

        String currentDatasourcePrefix = DATASOURCE_PROPERTY_PREFIX + "." + dataSourceName;

        Set<String> keys = this.configuration.subset(DEFAULT_PROPERTIES_KEY_PREFIX).getKeys();
        keys.addAll(this.configuration.subset(currentDatasourcePrefix).getKeys());

        for (String key : keys) {
            properties.put(key, this.configuration.getString(currentDatasourcePrefix + "." + key,
                    this.configuration.getString(DEFAULT_PROPERTIES_KEY_PREFIX + "." + key)));
        }

        return properties;
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder, Map<String, String> properties) {
        Iterator<Map.Entry<String, String>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            dataSourceBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, String dataSourceClassName) {
        //1.druid
        if ("com.alibaba.druid.pool.DruidDataSource".equals(dataSourceClassName)) {
            // init method
            beanDefinitionBuilder.setInitMethodName("init");
            // destroy method
            beanDefinitionBuilder.setDestroyMethodName("close");
        }
    }
}
