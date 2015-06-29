package message.mybatis.configs;

import message.mybatis.configs.parser.MyBatisConfigParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/26 上午12:17
 */
public class MyBatisNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("mybatis", new MyBatisConfigParser());
    }
}
