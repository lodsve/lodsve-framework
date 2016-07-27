package lodsve.core.config;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import lodsve.core.config.loader.i18n.DefaultResourceBundleMessageSource;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.stereotype.Component;


/**
 * 国际化资源文件工具类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-14 下午07:59:37
 */
@Component
public class MessageConfig implements ApplicationContextAware {
    //国际化资源文件处理类在spring上下文中的key
    private static final String DEFAULT_MESSAGE_SOURCE = "messageSource";

    private static DefaultResourceBundleMessageSource bundleMessageSource = null;
    private static final Object lockObject = new Object();
    private static Map<Locale, Map<String, String>> allMessagesMap = new HashMap<>();
    private static ApplicationContext context;

    @PostConstruct
    public void init() {
        synchronized (lockObject) {
            Object obj = context.getBean(DEFAULT_MESSAGE_SOURCE);

            if (obj instanceof DefaultResourceBundleMessageSource)
                bundleMessageSource = (DefaultResourceBundleMessageSource) obj;
            else if (obj instanceof DelegatingMessageSource) {
                DelegatingMessageSource delegatingMessageSource = (DelegatingMessageSource) obj;
                Object obj2 = delegatingMessageSource.getParentMessageSource();
                if (obj2 instanceof DefaultResourceBundleMessageSource)
                    bundleMessageSource = (DefaultResourceBundleMessageSource) obj2;
                else
                    throw new RuntimeException("can not find any messageSource what is DefaultResourceBundleMessageSource!");
            }
        }
    }

    /**
     * get message by locale, and format with args
     *
     * @param code           键
     * @param locale         语言
     * @param defaultMessage String to return if the lookup fails
     * @param args           参数
     * @return
     */
    public static String getMessage(String code, Locale locale, String defaultMessage, String... args) {
        return context.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * get message by locale, and format with args
     *
     * @param code   键
     * @param locale 语言
     * @param args   参数
     * @return
     */
    public static String getMessage(String code, Locale locale, String... args) {
        return context.getMessage(code, args, StringUtils.EMPTY, locale);
    }

    /**
     * get message by locale, and format with args
     *
     * @param code 键
     * @param args 参数
     * @return
     */
    public static String getMessage(String code, String... args) {
        return context.getMessage(code, args, StringUtils.EMPTY, Locale.getDefault());
    }

    /**
     * get message by locale, and format with args
     *
     * @param code 键
     * @return
     */
    public static String getMessage(String code) {
        return context.getMessage(code, Collections.emptyList().toArray(new String[0]), StringUtils.EMPTY, Locale.getDefault());
    }

    /**
     * 获取locale语言下所有的message
     *
     * @param locale
     * @return
     */
    public static Map<String, String> getMessages(Locale locale) {
        if (locale == null)
            locale = Locale.getDefault();

        Map<String, String> allMessages = allMessagesMap.get(locale);
        if (allMessages != null)
            return allMessages;

        ResourceBundle bundle = bundleMessageSource.getResourceBundle(locale);
        Enumeration<String> keys = bundle.getKeys();

        Map<String, String> messages = new HashMap<>();
        for (; keys.hasMoreElements(); ) {
            String key = keys.nextElement();
            String message = bundle.getString(key);

            messages.put(key, message);
        }

        allMessagesMap.put(locale, messages);

        return messages;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
