package lodsve.amqp.core;

/**
 * 可以使用spel获取rabbit的一些配置bean name.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/7 13:30
 */
public class RabbitUtils {
    public static final String DEFAULT_EXCHANGE_BEAN_NAME = "defaultDirectExchange";
    public static final String DEFAULT_MESSAGE_CONVERTER_BEAN_NAME = "defaultMessageConverter";
    public static final String CONNECTION_FACTORY_BEAN_NAME = "connectionFactory";

    /**
     * default exchange bean name
     *
     * @return bean name
     */
    public static String defaultExchange() {
        return DEFAULT_EXCHANGE_BEAN_NAME;
    }

    /**
     * default message converter bean name
     *
     * @return bean name
     */
    public static String defaultMessageConverter() {
        return DEFAULT_MESSAGE_CONVERTER_BEAN_NAME;
    }

    /**
     * connection factory bean name
     *
     * @return bean name
     */
    public static String connectionFactory() {
        return CONNECTION_FACTORY_BEAN_NAME;
    }
}
