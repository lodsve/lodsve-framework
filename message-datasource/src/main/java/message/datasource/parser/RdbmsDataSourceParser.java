package message.datasource.parser;

import message.config.SystemConfig;
import message.config.loader.properties.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 关系型数据库的数据源配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/21 下午5:28
 */
public class RdbmsDataSourceParser implements BeanDefinitionParser {
    private static final String DATASOURCE_FILE_NAME = "dataSource.properties";

    /**
     * 数据源配置的key
     */
    private static final String DATASOURCE_CLASS = "datasources.rdbms.dataSourceClass";
    private static final String DATASOURCE_PROPERTY_PREFIX = "datasources.rdbms";
    private static final String DEFAULT_PROPERTIES_KEY_PREFIX = DATASOURCE_PROPERTY_PREFIX + ".default";
    private static final String DATASOURCE_ELE_NAME = "name";

    private static Configuration configuration;

    static {
        configuration = SystemConfig.getFileConfiguration(DATASOURCE_FILE_NAME);
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        //注册数据源
        registerDataSource(element, registry);

        return null;
    }

    /**
     * 注册数据源到spring的上下文中
     *
     * @param element  xml的元素
     * @param registry spring bean的注册机
     */
    private void registerDataSource(Element element, BeanDefinitionRegistry registry) {
        //数据源名称
        String dataSourceName = getDataSourceName(element);
        //判断上下文中是否存在这个数据源
        boolean isExist = registry.containsBeanDefinition(dataSourceName);
        if (isExist)
            return;

        String dataSourceClassName = configuration.getString(DATASOURCE_CLASS);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        Map<String, String> properties = this.getProperties(dataSourceName);
        this.setDataSourceProperty(beanDefinitionBuilder, properties);

        this.setCustomProperties(beanDefinitionBuilder, dataSourceClassName);
        registry.registerBeanDefinition(dataSourceName, beanDefinitionBuilder.getBeanDefinition());
    }

    private String getDataSourceName(Element element) {
        return element.getAttribute(DATASOURCE_ELE_NAME);
    }

    private Map<String, String> getProperties(String dataSourceName) {
        Map<String, String> properties = new HashMap<String, String>();

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
