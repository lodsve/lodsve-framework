package lodsve.webservice;

import lodsve.core.bean.BeanRegisterUtils;
import lodsve.core.utils.StringUtils;
import org.apache.cxf.configuration.spring.AbstractBeanDefinitionParser;
import org.apache.cxf.configuration.spring.BusWiringType;
import org.apache.cxf.jaxws.spring.EndpointDefinitionParser.SpringEndpointImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Webservice Bean Definition Registrar.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/14 上午12:12
 */
public class WebserviceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String VALUE_ATTRIBUTE_NAME = "value";
    private static final String WIRE_BUS_ATTRIBUTE = AbstractBeanDefinitionParser.class.getName() + ".wireBus";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableWebService.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableWebService.class.getName(), importingClassMetadata.getClassName()));

        AnnotationAttributes[] webServices = attributes.getAnnotationArray(VALUE_ATTRIBUTE_NAME);

        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
        for (AnnotationAttributes webService : webServices) {
            String id = webService.getString("id");
            Class<?> clazz = webService.getClass("clazz");
            String address = webService.getString("address");

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringEndpointImpl.class);
            builder.addPropertyValue("address", address);
            builder.addPropertyValue("checkBlockConstruct", Boolean.TRUE);
            builder.getRawBeanDefinition().setAttribute(WIRE_BUS_ATTRIBUTE, BusWiringType.CONSTRUCTOR);
            builder.setInitMethodName("publish");
            builder.setDestroyMethodName("stop");
            builder.setLazyInit(false);

            String name;
            if (StringUtils.isNotBlank(id)) {
                name = id;
                builder.addConstructorArgReference(id);
            } else if (!Object.class.equals(clazz)) {
                name = clazz.getName();
                builder.addConstructorArgValue(BeanUtils.instantiate(clazz));
            } else {
                throw new IllegalArgumentException("id or clazz must give one!");
            }

            beanDefinitions.put(generateBeanName(name, registry), builder.getBeanDefinition());
        }

        BeanRegisterUtils.registerBeans(beanDefinitions, registry);
    }

    private String generateBeanName(String name, BeanDefinitionRegistry registry) {
        if (registry.containsBeanDefinition(name)) {
            name += "#endpoint";
        }

        return name;
    }
}
