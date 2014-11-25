package message.jdbc.configs.parser;

import message.config.SystemConfig;
import message.template.resource.ThymeleafTemplateResource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 关系型数据库数据源的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-10 上午1:39
 */
public class RdbmsDatasourceParser implements BeanDefinitionParser {
    private static final String RDBMS_TEMPLATE_LOCATION = "META-INF/template/jdbc.xml";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        dynamicLoadConfigBean(element, registry);

        return null;
    }

    private String getDataSourceName(Element element) {
        return element.getAttribute("dataSource");
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        Map<String, String> context = new HashMap<String, String>();
        context.put("dataSource", getDataSourceName(element));
        context.put("basePackage", element.getAttribute("basePackage"));

        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        beanDefinitionReader.loadBeanDefinitions(new ThymeleafTemplateResource(RDBMS_TEMPLATE_LOCATION, context, "xml"));
    }
}
