package message.jdbc.configs;

import message.jdbc.configs.parser.DataSourceParser;
import message.jdbc.configs.parser.RdbmsDatasourceParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 数据源命名空间的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-10 上午1:37
 */
public class DatasourceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("dataSource", new DataSourceParser());
        registerBeanDefinitionParser("jdbc", new RdbmsDatasourceParser());
    }
}
