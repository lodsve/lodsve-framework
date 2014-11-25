package message.search.configs.parser;

import message.template.resource.ThymeleafTemplateResource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * 解析搜索引擎配置文件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-14 下午3:32
 */
public abstract class AbstractSearchEngineParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean的注册
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        dynamicLoadConfigBean(element, registry);
        return null;
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        beanDefinitionReader.loadBeanDefinitions(this.getConfigFileResource(this.getTemplatePath(), getConfigs(element)));
    }

    private Resource getConfigFileResource(String templatePath, Map<String, String> context) {
        return new ThymeleafTemplateResource(templatePath, context, "xml");
    }

    /**
     * 获取每种配置文件中的字段
     *
     * @param element
     * @return
     */
    protected abstract Map<String, String> getConfigs(Element element);

    /**
     * 获取模板文件位置
     *
     * @return
     */
    protected abstract String getTemplatePath();
}
