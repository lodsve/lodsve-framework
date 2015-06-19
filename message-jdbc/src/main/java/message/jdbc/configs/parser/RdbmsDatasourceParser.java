package message.jdbc.configs.parser;

import message.config.SystemConfig;
import message.config.properties.Configuration;
import message.template.resource.ThymeleafTemplateResource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.io.File;
import java.util.*;

/**
 * 关系型数据库数据源的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-10 上午1:39
 */
public class RdbmsDatasourceParser implements BeanDefinitionParser {
    private static final String RDBMS_TEMPLATE_LOCATION = "META-INF/template/jdbc.xml";
    /**
     * 数据源配置的key
     */
    private static final String DATASOURCE_CLASS = "datasources.rdbms.dataSourceClass";
    private static final String DATASOURCE_PROPERTY_PREFIX = "datasources.rdbms";
    private static final String DEFAULT_PROPERTIES_KEY_PREFIX = DATASOURCE_PROPERTY_PREFIX + ".default";
    private static Configuration configuration;

    static {
        configuration = SystemConfig.getFileConfiguration("datasource" + File.separator + "rdbms.properties");
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        //注册数据源
        registDataSource(element, registry);
        //动态注册jdbc的相关配置
        dynamicLoadConfigBean(element, registry);

        return null;
    }

    private String getDataSourceName(Element element) {
        return element.getAttribute("dataSource");
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("dataSource", getDataSourceName(element));
        context.put("basePackage", element.getAttribute("basePackage"));
        context.put("useFlyway", element.getAttribute("useFlyway"));
        context.put("migration", element.getAttribute("migration"));
        context.put("dbType", element.getAttribute("dbType"));

        List<Element> childElements = DomUtils.getChildElementsByTagName(element, "convert");
        Map<String, String> converts = new HashMap<String, String>();
        for (Element ele : childElements) {
            converts.put(ele.getAttribute("key"), ele.getAttribute("value"));
        }

        context.put("converts", converts);
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        Resource resource = new ThymeleafTemplateResource(RDBMS_TEMPLATE_LOCATION, context, "xml");
        beanDefinitionReader.loadBeanDefinitions(resource);
    }

    /**
     * 注册数据源到spring的上下文中
     *
     * @param element  xml的元素
     * @param registry spring bean的注册机
     */
    private void registDataSource(Element element, BeanDefinitionRegistry registry) {
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
