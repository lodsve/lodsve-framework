package message.datasource;

import message.datasource.parser.MongoDbDataSourceParser;
import message.datasource.parser.RdbmsDataSourceParser;
import message.datasource.parser.RedisDataSourceParser;
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
        //关系型数据库
        registerBeanDefinitionParser("rdbms", new RdbmsDataSourceParser());
        //mongo
        registerBeanDefinitionParser("mongo", new MongoDbDataSourceParser());
        //redis
        registerBeanDefinitionParser("redis", new RedisDataSourceParser());
    }
}
