package lodsve.mybatis.datasource.builder;

import lodsve.core.autoconfigure.AutoConfigurationBuilder;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.mybatis.configs.Constant;
import lodsve.mybatis.configs.RdbmsProperties;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午8:35
 */
public abstract class AbstractDataSource<T> {
    private String dataSourceName;
    RdbmsProperties rdbmsProperties;

    AbstractDataSource(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        AutoConfigurationBuilder.Builder<RdbmsProperties> builder = new AutoConfigurationBuilder.Builder<>();
        builder.setAnnotation(RdbmsProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(RdbmsProperties.class);

        rdbmsProperties = builder.build();
    }

    Map<String, String> getProperties() {
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

    /**
     * 创建数据源
     *
     * @return 数据源
     */
    abstract T build();
}
