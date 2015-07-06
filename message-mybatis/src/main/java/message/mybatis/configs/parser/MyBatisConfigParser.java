package message.mybatis.configs.parser;

import message.template.resource.ThymeleafTemplateResource;
import message.utils.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatis的一些配置动态引入到spring上下文.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/26 上午12:18
 */
public class MyBatisConfigParser implements BeanDefinitionParser {
    private static final String MYBATIS_TEMPLATE_LOCATION = "META-INF/template/mybatis.xml";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        //动态注册mybatis的相关配置
        dynamicLoadConfigBean(element, registry);

        return null;
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("dataSource", element.getAttribute("dataSource"));
        String basePackage = element.getAttribute("basePackage");
        context.put("basePackage", basePackage);
        context.put("useFlyway", element.getAttribute("useFlyway"));
        context.put("migration", element.getAttribute("migration"));
        context.put("dbType", element.getAttribute("dbType"));

        String typeHandlersLocations = element.getAttribute("typeHandlersLocations");
        if(StringUtils.isEmpty(typeHandlersLocations)) {
            typeHandlersLocations = basePackage + ".dao.types";
        }
        context.put("typeHandlersLocations", typeHandlersLocations);

        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        Resource resource = new ThymeleafTemplateResource(MYBATIS_TEMPLATE_LOCATION, context, "xml");
        beanDefinitionReader.loadBeanDefinitions(resource);
    }
}
