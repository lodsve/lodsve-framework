package lodsve.mybatis.configs;

import lodsve.core.autoconfigure.AutoConfigurationCreator;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 关系型数据库数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:00
 */
public class RdbmsDataSourceBeanDefinitionFactory {
    public static final Logger logger = LoggerFactory.getLogger(RdbmsDataSourceBeanDefinitionFactory.class);
    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
    public static final String DBCP_DATA_SOURCE_CLASS = "org.apache.commons.dbcp.BasicDataSource";

    private String dataSourceName;
    private RdbmsProperties rdbmsProperties;

    public RdbmsDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        AutoConfigurationCreator.Builder<RdbmsProperties> builder = new AutoConfigurationCreator.Builder<>();
        builder.setAnnotation(RdbmsProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(RdbmsProperties.class);

        try {
            rdbmsProperties = builder.build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            this.rdbmsProperties = new RdbmsProperties();
        }
    }

    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        setDataSourceProperty(beanDefinitionBuilder);
        setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder) {
        // 通用配置
        RdbmsProperties.DataSourceSetting commons = rdbmsProperties.getCommons();
        // dbcp配置
        RdbmsProperties.DbcpSetting dbcp = rdbmsProperties.getDbcp();
        // druid配置
        RdbmsProperties.DruidSetting druid = rdbmsProperties.getDruid();
        // 连接信息
        RdbmsProperties.RdbmsConnection connection = rdbmsProperties.getConnections().get(dataSourceName);

        Map<String, String> properties = new HashMap<>(16);
        properties.putAll(toMap(commons));
        properties.putAll(toMap(connection));

        if (DRUID_DATA_SOURCE_CLASS.equals(rdbmsProperties.getDataSourceClass())) {
            properties.putAll(toMap(druid));
        } else if (DBCP_DATA_SOURCE_CLASS.equals(rdbmsProperties.getDataSourceClass())) {
            properties.putAll(toMap(dbcp));
        }

        setDataSourceProperty(dataSourceBuilder, properties);
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder, Map<String, String> properties) {
        Iterator<Map.Entry<String, String>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            dataSourceBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private Map<String, String> toMap(Object object) {
        BeanWrapper wrapper = new BeanWrapperImpl(object);
        PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();
        Map<String, String> properties = new HashMap<>(descriptors.length);
        for (PropertyDescriptor d : descriptors) {
            if (d.getWriteMethod() == null) {
                continue;
            }

            String name = d.getName();
            Object value = wrapper.getPropertyValue(name);

            properties.put(name, value.toString());
        }

        return properties;
    }

    private void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, String dataSourceClassName) {
        //1.druid
        if (DRUID_DATA_SOURCE_CLASS.equals(dataSourceClassName)) {
            // init method
            beanDefinitionBuilder.setInitMethodName("init");
            // destroy method
            beanDefinitionBuilder.setDestroyMethodName("close");
        }
    }
}
