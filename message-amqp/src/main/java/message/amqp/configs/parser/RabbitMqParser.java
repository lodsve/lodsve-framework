package message.amqp.configs.parser;

import message.config.SystemConfig;
import message.template.resource.ThymeleafTemplateResource;
import message.utils.PropertyPlaceholderHelper;
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
 * RabbitMq配置的处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-10 上午1:39
 */
public class RabbitMqParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        dynamicLoadConfigBean(element, registry);
        return null;
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        //判断上下文中是否存在
        boolean isExist = registry.containsBeanDefinition("rabbitConnectionFactory");
        if (isExist)
            return;

        beanDefinitionReader.loadBeanDefinitions(this.getConfigFileResource("META-INF/template/amqp.xml", getConfigs(element)));
    }

    private Resource getConfigFileResource(String templatePath, Map<String, String> context) {
        Resource resource = new ThymeleafTemplateResource(templatePath, context, "xml");
        return resource;
    }

    /**
     * 获取配置文件中的字段
     *
     * @param element
     * @return
     */
    protected Map<String, String> getConfigs(Element element) {
        String address = element.getAttribute("addresses");
        String username = element.getAttribute("username");
        String password = element.getAttribute("password");
        String exchange = element.getAttribute("exchange");

        Map<String, String> params = new HashMap<String, String>();

        params.put("addresses", PropertyPlaceholderHelper.processProperties(address, false, SystemConfig.getAllConfigs()));
        params.put("username", PropertyPlaceholderHelper.processProperties(username, false, SystemConfig.getAllConfigs()));
        params.put("password", PropertyPlaceholderHelper.processProperties(password, false, SystemConfig.getAllConfigs()));
        params.put("exchange", PropertyPlaceholderHelper.processProperties(exchange, false, SystemConfig.getAllConfigs()));
        return params;
    }
}
