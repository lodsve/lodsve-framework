package lodsve.mybatis.configs;

import lodsve.core.bean.BeanRegisterUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 动态创建mybatis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        MyBatisConfigurationBuilder.Builder builder = new MyBatisConfigurationBuilder.Builder();

        BeanRegisterUtils.registerBeans(builder.setMetadata(metadata).build(), registry);
    }
}
