package message.datasource.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * redis数据源配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/24 上午11:08
 */
public class RedisDataSourceParser extends BaseDataSourceParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return null;
    }
}
