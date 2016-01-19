package message.datasource.config.parser;

import message.datasource.core.DataSourceBeanDefinitionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 关系型数据库的数据源配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/21 下午5:28
 */
public class RdbmsDataSourceParser implements BeanDefinitionParser {
    private static final String DATASOURCE_ELE_NAME = "name";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        String dataSourceName = getDataSourceName(element);

        //判断上下文中是否存在这个数据源
        if (registry.containsBeanDefinition(dataSourceName)) {
            return null;
        }

        //注册数据源
        BeanDefinition beanDefinition = new DataSourceBeanDefinitionFactory(dataSourceName).build();
        registry.registerBeanDefinition(dataSourceName, beanDefinition);

        return null;
    }

    private String getDataSourceName(Element element) {
        return element.getAttribute(DATASOURCE_ELE_NAME);
    }
}
