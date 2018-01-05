package lodsve.core.properties.autoconfigure;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 在properties bean初始化前设置配置文件中的值.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/9 下午5:41
 */
public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor {

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);

        if (annotation == null) {
            return bean;
        }

        return new AutoConfigurationBuilder.Builder(bean.getClass()).build();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
