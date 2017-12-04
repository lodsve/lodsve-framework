package lodsve.dfs.configuration;

import lodsve.dfs.annotations.EnableFs;
import lodsve.dfs.enums.FsType;
import lodsve.dfs.service.FsService;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 动态注册bean.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 11:20
 */
public class FsBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String VALUE_ATTRIBUTE_NAME = "value";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableFs.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableFs.class.getName(), annotationMetadata.getClassName()));

        FsType type = attributes.getEnum(VALUE_ATTRIBUTE_NAME);
        Class<? extends FsService> clazz = type != null ? type.getImplClazz() : null;

        if (clazz == null) {
            return;
        }

        BeanDefinitionBuilder fsServiceBean = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionRegistry.registerBeanDefinition("fsService", fsServiceBean.getBeanDefinition());
    }
}
