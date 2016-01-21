package message.config.auto;

import message.config.auto.annotations.ConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/9 下午5:41
 */
@Component
@DependsOn({"configurationLoader", "iniLoader", "messageSourceLoader"})
public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);

        Object target = bean;
        if (annotation != null) {
            try {
                target = new AutoConfigurationCreator(null).createBean(target.getClass(), annotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return target;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
