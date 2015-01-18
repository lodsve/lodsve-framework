package message.exception.configs;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 异常类型映射命名空间的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-10 上午1:37
 */
public class ExceptionMappingNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("exception", new ExceptionMappingParser());
    }
}
