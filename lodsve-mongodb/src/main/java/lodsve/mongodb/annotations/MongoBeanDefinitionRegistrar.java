package lodsve.mongodb.annotations;

import lodsve.core.utils.StringUtils;
import lodsve.mongodb.core.MongoDataSourceBeanDefinitionFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.context.MappingContextIsNewStrategyFactory;
import org.springframework.data.mongodb.config.WriteConcernPropertyEditor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 加载mongodb操作的一些bean.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/21 下午10:15
 */
public class MongoBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String MAPPING_CONTEXT_BEAN_NAME = "mongoMappingContext";
    private static final String MAPPING_CONVERTER_BEAN_NAME_SUBFIX = "MappingConverter";
    private static final String IS_NEW_STRATEGY_FACTORY_BEAN_NAME = "isNewStrategyFactory";
    private static final String DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME = "defaultMongoTypeMapper";
    private static final String INDEX_HELPER_BEAN_NAME = "indexCreationHelper";
    private static final String CUSTOM_EDITOR_CONFIGURER_BEAN_NAME = "customEditorConfigurer";
    private static final String MONGO_TEMPLATE_BEAN_NAME_SUBFIX = "MongoTemplate";

    private static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private static final String DOMAIN_PACKAGE_ATTRIBUTE_NAME = "domainPackage";

    private static final Map<String, BeanDefinition> BEAN_DEFINITION_MAP = new HashMap<>(16);

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

        String converterId = dataSource + MAPPING_CONVERTER_BEAN_NAME_SUBFIX;
        String contextId = converterId + "." + MAPPING_CONTEXT_BEAN_NAME;
        String templateId = dataSource + MONGO_TEMPLATE_BEAN_NAME_SUBFIX;

        initMongoMappingContext(converterId, domainPackage);
        initMappingContextIsNewStrategyFactory(contextId);
        initMappingConverter(dataSource, contextId, converterId);
        initMongoPersistentEntityIndexCreator(contextId, dataSource);
        initMongoTemplate(converterId, dataSource, templateId);
    }

    private void initMappingConverter(String dataSource, String contextId, String converterId) {
        BeanDefinitionBuilder converterBuilder = BeanDefinitionBuilder.genericBeanDefinition(MappingMongoConverter.class);
        converterBuilder.addConstructorArgReference(dataSource);
        converterBuilder.addConstructorArgReference(contextId);
        converterBuilder.addPropertyReference("typeMapper", DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME);

        BEAN_DEFINITION_MAP.put(converterId, converterBuilder.getBeanDefinition());
    }

    private void initMongoMappingContext(String contextId, String[] domainPackage) {
        BeanDefinitionBuilder mappingContextBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoMappingContext.class);
        Set<String> classesToAdd = getInititalEntityClasses(domainPackage);
        if (classesToAdd != null) {
            mappingContextBuilder.addPropertyValue("initialEntitySet", classesToAdd);
        }

        BEAN_DEFINITION_MAP.put(contextId, mappingContextBuilder.getBeanDefinition());
    }

    private void initMongoPersistentEntityIndexCreator(String contextId, String dataSource) {
        BeanDefinitionBuilder indexHelperBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoPersistentEntityIndexCreator.class);
        indexHelperBuilder.addConstructorArgReference(contextId);
        indexHelperBuilder.addConstructorArgReference(dataSource);
        indexHelperBuilder.addDependsOn(contextId);

        BEAN_DEFINITION_MAP.put(INDEX_HELPER_BEAN_NAME, indexHelperBuilder.getBeanDefinition());
    }

    private static Set<String> getInititalEntityClasses(String[] domainPackages) {
        if (ArrayUtils.isEmpty(domainPackages)) {
            return null;
        }

        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

        Set<String> classes = new ManagedSet<>();
        for (String domainPackage : domainPackages) {
            if (StringUtils.isBlank(domainPackage)) {
                continue;
            }
            for (BeanDefinition candidate : componentProvider.findCandidateComponents(domainPackage)) {
                classes.add(candidate.getBeanClassName());
            }
        }

        return classes;
    }

    private void initMappingContextIsNewStrategyFactory(String mappingContextRef) {
        BeanDefinitionBuilder mappingContextStrategyFactoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(MappingContextIsNewStrategyFactory.class);
        mappingContextStrategyFactoryBuilder.addConstructorArgReference(mappingContextRef);

        BEAN_DEFINITION_MAP.put(IS_NEW_STRATEGY_FACTORY_BEAN_NAME, mappingContextStrategyFactoryBuilder.getBeanDefinition());
    }

    private void initMongoTemplate(String converterId, String dataSource, String templateId) {
        BeanDefinitionBuilder mongoTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoTemplate.class);
        mongoTemplateBuilder.addConstructorArgReference(dataSource);
        mongoTemplateBuilder.addConstructorArgReference(converterId);

        BeanDefinitionBuilder writeConcernPropertyEditorBuilder = getWriteConcernPropertyEditorBuilder();

        BEAN_DEFINITION_MAP.put(CUSTOM_EDITOR_CONFIGURER_BEAN_NAME, writeConcernPropertyEditorBuilder.getBeanDefinition());
        BEAN_DEFINITION_MAP.put(templateId, mongoTemplateBuilder.getBeanDefinition());
    }

    private BeanDefinitionBuilder getWriteConcernPropertyEditorBuilder() {
        Map<String, Class<?>> customEditors = new ManagedMap<>();
        customEditors.put("com.mongodb.WriteConcern", WriteConcernPropertyEditor.class);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CustomEditorConfigurer.class);
        builder.addPropertyValue("customEditors", customEditors);

        return builder;
    }

    private void initMongoRepository(){

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
