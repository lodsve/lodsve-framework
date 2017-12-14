package lodsve.mybatis.configs;

import lodsve.core.bean.BeanRegisterUtils;
import lodsve.mybatis.datasource.MyBatisConfigutaionBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态创建mybatis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
        MyBatisConfigutaionBuilder.Builder builder = new MyBatisConfigutaionBuilder.Builder();
        beanDefinitions.putAll(builder.setMetadata(metadata).build());
        BeanRegisterUtils.registerBeans(beanDefinitions, registry);
    }
}
