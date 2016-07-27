package lodsve.dubbo.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.core.template.ThymeleafTemplateResource;
import lodsve.core.utils.StringUtils;
import lodsve.core.config.auto.AutoConfigurationCreator;
import lodsve.core.config.auto.annotations.ConfigurationProperties;
import lodsve.dubbo.config.DubboProperties;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 加载dubbo的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-4 13:42
 */
public class DubboConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(DubboConfigurationRegistrar.class);

    private static final String APPLICATION_ATTRIBUTE_NAME = "application";
    private static final String SCAN_PACKAGES_ATTRIBUTE_NAME = "scanPackages";
    private static final String PRODUCERS_ATTRIBUTE_NAME = "producers";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableDubbo.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableDubbo.class.getName(), annotationMetadata.getClassName()));

        String application = attributes.getString(APPLICATION_ATTRIBUTE_NAME);
        String[] scanPackages = attributes.getStringArray(SCAN_PACKAGES_ATTRIBUTE_NAME);
        List<String> producers = getProductors(attributes);

        if (ArrayUtils.isEmpty(scanPackages)) {
            scanPackages = findDefaultPackage(annotationMetadata);
        }

        try {
            DubboProperties properties = getDubboProperties();
            Map<String, Object> context = new HashMap<>();
            context.put("application", application);
            context.put("registry", properties.getRegistry().get(application));
            context.put("scanPackages", StringUtils.join(scanPackages, ","));
            context.put("producers", getProductorsInfo(producers, properties));

            BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanDefinitionRegistry);
            beanDefinitionReader.loadBeanDefinitions(loadBeanDefinitions(context));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private Resource loadBeanDefinitions(Map<String, Object> context) {
        return new ThymeleafTemplateResource("META-INF/template/dubbo.xml", context, "xml");
    }

    private String[] findDefaultPackage(AnnotationMetadata metadata) {
        String clazzName = metadata.getClassName();
        List<String> scanPackages = new ArrayList<>();
        try {
            Class<?> clazz = ClassUtils.forName(clazzName, getClass().getClassLoader());
            scanPackages.add(ClassUtils.getPackageName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return scanPackages.toArray(new String[scanPackages.size()]);
    }

    private DubboProperties getDubboProperties() throws Exception {
        AutoConfigurationCreator.Builder<DubboProperties> builder = new AutoConfigurationCreator.Builder<>();
        builder.setAnnotation(DubboProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(DubboProperties.class);

        return builder.build();
    }

    private List<String> getProductors(AnnotationAttributes attributes) {
        AnnotationAttributes[] producers = attributes.getAnnotationArray(PRODUCERS_ATTRIBUTE_NAME);
        List<String> producerNames = new ArrayList<>(producers.length);
        for (AnnotationAttributes p : producers) {
            String name = p.getString(APPLICATION_ATTRIBUTE_NAME);
            if (StringUtils.isNotBlank(name)) {
                producerNames.add(p.getString(APPLICATION_ATTRIBUTE_NAME));
            }
        }

        return producerNames;
    }

    private Map<String, DubboProperties.Registry> getProductorsInfo(List<String> productors, DubboProperties properties) {
        Map<String, DubboProperties.Registry> productorsInfo = new HashMap<>(productors.size());
        for (String productor : productors) {
            DubboProperties.Registry registry = properties.getRegistry().get(productor);
            if (registry != null) {
                productorsInfo.put(productor, registry);
            }
        }

        return productorsInfo;
    }
}
