package message.mvc.configs;

import message.mvc.configs.parser.MVCConfigParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * mvc模块配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-24 19:31
 */
public class MVCNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("mvc", new MVCConfigParser());
    }
}
