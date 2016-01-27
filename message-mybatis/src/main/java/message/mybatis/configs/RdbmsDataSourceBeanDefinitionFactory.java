package message.mybatis.configs;

import message.config.auto.AutoConfigurationCreator;
import message.config.auto.annotations.ConfigurationProperties;
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
    private String dataSourceName;
    private RdbmsProperties rdbmsProperties;

    public RdbmsDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        try {
            AutoConfigurationCreator.Builder<RdbmsProperties> builder = new AutoConfigurationCreator.Builder<>();
            builder.setAnnotation(RdbmsProperties.class.getAnnotation(ConfigurationProperties.class));
            builder.setClazz(RdbmsProperties.class);

            rdbmsProperties = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        Map<String, String> properties = this.getProperties(dataSourceName);
        this.setDataSourceProperty(beanDefinitionBuilder, properties);

        this.setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Map<String, String> getProperties(String dataSourceName) {
        Map<String, String> properties = new HashMap<>();

        RdbmsProperties.RdbmsConnection connection = rdbmsProperties.getConnections().get(dataSourceName);
        RdbmsProperties.DataSourceSetting setting = rdbmsProperties.getDefaults();

        BeanWrapper connectionWrapper = new BeanWrapperImpl(connection);
        BeanWrapper settingWrapper = new BeanWrapperImpl(setting);

        PropertyDescriptor[] descriptors = connectionWrapper.getPropertyDescriptors();
        for (PropertyDescriptor d : descriptors) {
            if (d.getWriteMethod() == null) {
                continue;
            }

            String name = d.getName();
            Object value = connectionWrapper.getPropertyValue(name);

            if (value != null) {
                properties.put(name, value.toString());
            } else {
                value = settingWrapper.getPropertyValue(name);
                if (value != null) {
                    properties.put(name, value.toString());
                }
            }
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
