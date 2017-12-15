package lodsve.mybatis.datasource.builder;

import lodsve.mybatis.configs.Constant;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Map;

/**
 * 关系型数据库数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:00
 */
public class RdbmsDataSourceBeanDefinitionFactory extends AbstractDataSource<BeanDefinition> {
    public RdbmsDataSourceBeanDefinitionFactory(String dataSourceName) {
        super(dataSourceName);
    }

    @Override
    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        setDataSourceProperty(beanDefinitionBuilder);
        setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
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
