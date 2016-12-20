package lodsve.core.context;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 非web应用spring的上下文.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:20
 */
class SpringContext {
    private SpringContext() {
    }

    static class Builder {
        private String configLocation = "application-context.xml";
        private Object bean;

        Builder config(String configLocation) {
            this.configLocation = configLocation;
            return this;
        }

        Builder bean(Object bean) {
            this.bean = bean;
            return this;
        }

        void build() {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);

            ApplicationHelper.getInstance().addApplicationContext(context);
        }
    }
}
