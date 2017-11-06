package lodsve.mongodb.annotations;

import lodsve.core.template.ThymeleafTemplateResource;
import lodsve.core.utils.StringUtils;
import lodsve.mongodb.core.MongoDataSourceBeanDefinitionFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
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
    private static final String DOMAIN_PACKAGE_ATTRIBUTE_NAME = "domainPackage";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMongo.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMongo.class.getName(), importingClassMetadata.getClassName()));

        // 注册数据源
        String dataSource = attributes.getString(DATA_SOURCE_ATTRIBUTE_NAME);
        registry.registerBeanDefinition(dataSource, new MongoDataSourceBeanDefinitionFactory(dataSource).build());

        String[] basePackage = attributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        String[] domainPackage = attributes.getStringArray(DOMAIN_PACKAGE_ATTRIBUTE_NAME);

        if (ArrayUtils.isEmpty(basePackage)) {
            basePackage = findDefaultPackage(importingClassMetadata);
        }
        if (ArrayUtils.isEmpty(domainPackage)) {
            domainPackage = findDefaultPackage(importingClassMetadata);
        }

        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        beanDefinitionReader.loadBeanDefinitions(loadBeanDefinitions(dataSource, basePackage, domainPackage));
    }

    private Resource loadBeanDefinitions(String dataSource, String[] basePackage, String[] domainPackage) {
        Map<String, Object> context = new HashMap<>(3);
        context.put("dataSource", dataSource);
        context.put("basePackage", StringUtils.join(Arrays.asList(basePackage), ","));
        context.put("domainPackage", StringUtils.join(Arrays.asList(domainPackage), ","));

        return new ThymeleafTemplateResource("META-INF/template/mongo.xml", context, "xml");
    }

    private String[] findDefaultPackage(AnnotationMetadata importingClassMetadata) {
        String className = importingClassMetadata.getClassName();
        try {
            Class<?> clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
            return new String[]{ClassUtils.getPackageName(clazz)};
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
