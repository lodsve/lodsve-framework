package message.exception.configs;

import message.config.SystemConfig;
import message.template.resource.ThymeleafTemplateResource;
import message.utils.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态注册异常类型与页面的关系.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-18 23:57
 */
public class ExceptionMappingParser implements BeanDefinitionParser {
    private static final String EXCEPTION_MAPPING_TEMPLATE_LOCATION = "META-INF/template/exception.xml";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        dynamicLoadConfigBean(element, registry);

        return null;
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        Map<String, Object> context = new HashMap<String, Object>();
        //spring.xml配置中优先级比较高
        String defaultErrorView = element.getAttribute("defaultErrorView");
        //配置文件中优先级比较低
        String defaultErrorViewInConfig = SystemConfig.getString("default.error.page");
        if (StringUtils.isNotEmpty(defaultErrorView)) {
            context.put("defaultErrorView", defaultErrorView);
        } else if (StringUtils.isNotEmpty(defaultErrorViewInConfig)) {
            context.put("defaultErrorView", defaultErrorViewInConfig);
        }

        List<Element> childElements = DomUtils.getChildElementsByTagName(element, "mappings");
        Map<String, String> mappings = new HashMap<String, String>();
        for (Element ele : childElements) {
            mappings.put(ele.getAttribute("key"), ele.getAttribute("value"));
        }

        context.put("mappings", mappings);
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        Resource resource = new ThymeleafTemplateResource(EXCEPTION_MAPPING_TEMPLATE_LOCATION, context, "xml");
        beanDefinitionReader.loadBeanDefinitions(resource);
    }
}
