package message.mongodb.annotations;

import message.template.resource.ThymeleafTemplateResource;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载mongodb操作的一些bean.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午10:15
 */
public class MongoBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMongo.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMongo.class.getName(), importingClassMetadata.getClassName()));

        String dataSource = attributes.getString(DATA_SOURCE_ATTRIBUTE_NAME);
        Assert.isTrue(registry.containsBeanDefinition(dataSource), String.format("Can not find any DataSource Bean named '%s' in context!", dataSource));
        String basePackage = attributes.getString(BASE_PACKAGE_ATTRIBUTE_NAME);

        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        beanDefinitionReader.loadBeanDefinitions(loadBeanDefinitions(dataSource, basePackage));
    }

    private Resource loadBeanDefinitions(String dataSource, String basePackage) {
        Map<String, Object> context = new HashMap<>();
        context.put("dataSource", dataSource);
        context.put("basePackage", basePackage);

        return new ThymeleafTemplateResource("META-INF/template/mongo.xml", context, "xml");
    }
}
