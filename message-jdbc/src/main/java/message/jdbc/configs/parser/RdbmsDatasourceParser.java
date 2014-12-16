package message.jdbc.configs.parser;

import message.jdbc.convert.ConvertBean;
import message.template.resource.ThymeleafTemplateResource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("dataSource", getDataSourceName(element));
        context.put("basePackage", element.getAttribute("basePackage"));
        context.put("useFlyway", element.getAttribute("useFlyway"));
        context.put("migration", element.getAttribute("migration"));

        List<Element> childElements = DomUtils.getChildElementsByTagName(element, "convert");
        List<ConvertBean> converts = new ArrayList<ConvertBean>();
        for(Element ele : childElements){
            ConvertBean bean = new ConvertBean();
            bean.setClazz(ele.getAttribute("key"));
            bean.setConvert(ele.getAttribute("value"));

            converts.add(bean);
        }

        context.put("converts", converts);
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        Resource resource = new ThymeleafTemplateResource(RDBMS_TEMPLATE_LOCATION, context, "xml");

        try {
            FileCopyUtils.copy(resource.getInputStream(), new FileOutputStream(new File("D:\\2.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        beanDefinitionReader.loadBeanDefinitions(resource);
    }
}
