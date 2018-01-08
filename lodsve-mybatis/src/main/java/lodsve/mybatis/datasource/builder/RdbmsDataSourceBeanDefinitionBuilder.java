package lodsve.mybatis.datasource.builder;

import lodsve.core.properties.autoconfigure.PropertiesConfigurationFactory;
import lodsve.mybatis.configs.Constant;
import lodsve.mybatis.properties.RdbmsProperties;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午8:35
 */
public class RdbmsDataSourceBeanDefinitionBuilder {
    private String dataSourceName;
    private RdbmsProperties rdbmsProperties;

    public RdbmsDataSourceBeanDefinitionBuilder(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        rdbmsProperties = new PropertiesConfigurationFactory.Builder<>(RdbmsProperties.class).build();
    }

    /**
     * 创建数据源
     *
     * @return 数据源
     */
    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        setDataSourceProperty(beanDefinitionBuilder);
        setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Map<String, String> getProperties() {
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

        if (Constant.DRUID_DATA_SOURCE_CLASS.equals(rdbmsProperties.getDataSourceClass())) {
            properties.putAll(toMap(druid));
        } else if (Constant.DBCP_DATA_SOURCE_CLASS.equals(rdbmsProperties.getDataSourceClass())) {
            properties.putAll(toMap(dbcp));
        }

        return properties;
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

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder) {
        setDataSourceProperty(dataSourceBuilder, getProperties());
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            dataSourceBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, String dataSourceClassName) {
        //1.druid
        if (Constant.DRUID_DATA_SOURCE_CLASS.equals(dataSourceClassName)) {
            // init method
            beanDefinitionBuilder.setInitMethodName("init");
            // destroy method
            beanDefinitionBuilder.setDestroyMethodName("close");
        }
    }
}
