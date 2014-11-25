package message.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <b>普通Java类获取Spring的ApplicationContext</b><br/>
 * 在web应用中，某些情况我们需要在类中来获得spring所管理的bean</br>
 * 可以使用<code>ApplicationContext context = new ClassPathXmlApplicationContext("classpath:**.xml")</code>
 * 来获取<br/>
 * 但是每次都得创建，比较繁琐也会耗资源<br/>
 * 所以就有了下面这个工具类<br/>
 * 创建一个类并让其实现org.springframework.context.ApplicationContextAware接口来让Spring在启动的时候为我们注入
 * ApplicationContext对象.
 *
 * @author sunhao(sunhao.java@gmail.com)
 */
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;//声明一个静态变量保存

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

}
