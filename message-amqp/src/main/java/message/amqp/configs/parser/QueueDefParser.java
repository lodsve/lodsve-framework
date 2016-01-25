package message.amqp.configs.parser;

import message.amqp.core.Queue;
import message.config.SystemConfig;
import message.base.template.ThymeleafTemplateResource;
import message.base.utils.PropertyPlaceholderHelper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * queue def parser.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-10-7 09:22
 */
public class QueueDefParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //bean对象注册机
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        dynamicLoadConfigBean(element, registry);
        return null;
    }

    private void dynamicLoadConfigBean(Element element, BeanDefinitionRegistry registry) {
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        beanDefinitionReader.loadBeanDefinitions(this.getConfigFileResource("META-INF/template/queue-def.xml", getConfigs(element)));
    }

    private Resource getConfigFileResource(String templatePath, Map<String, Object> context) {
        Resource resource = new ThymeleafTemplateResource(templatePath, context, "xml");
        return resource;
    }

    /**
     * 获取配置文件中的字段
     *
     * @param element
     * @return
     */
    protected Map<String, Object> getConfigs(Element element) {
        Map<String, String> configs = SystemConfig.getAllConfigs();

        List<Element> childElements = DomUtils.getChildElementsByTagName(element, "queue");

        List<Queue> queues = new ArrayList<Queue>();
        for(Element ele : childElements){
            Queue queue = new Queue();
            queue.setName(PropertyPlaceholderHelper.replacePlaceholder(ele.getAttribute("queue-name"), false, configs));
            queue.setExchange(PropertyPlaceholderHelper.replacePlaceholder(ele.getAttribute("exchange"), false, configs));
            queue.setRoutingKey(PropertyPlaceholderHelper.replacePlaceholder(ele.getAttribute("routing-key"), false, configs));
            queue.setRef(PropertyPlaceholderHelper.replacePlaceholder(ele.getAttribute("ref"), false, configs));
            queue.setMethod(PropertyPlaceholderHelper.replacePlaceholder(ele.getAttribute("method"), false, configs));

            queues.add(queue);
        }

        Map<String, Object> params = new HashMap<>();

        params.put("queues", queues);

        String rejected = PropertyPlaceholderHelper.replacePlaceholder(element.getAttribute("requeue-rejected"), false, configs);
        String converter = PropertyPlaceholderHelper.replacePlaceholder(element.getAttribute("message-converter"), false, configs);
        String handler = PropertyPlaceholderHelper.replacePlaceholder(element.getAttribute("error-handler"), false, configs);
        params.put("rejected", rejected);
        params.put("converter", converter);
        params.put("handler", handler);

        return params;
    }
}
