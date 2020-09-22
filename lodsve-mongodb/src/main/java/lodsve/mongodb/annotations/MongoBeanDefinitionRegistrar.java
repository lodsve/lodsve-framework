/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.mongodb.annotations;

import lodsve.core.bean.BeanRegisterUtils;
import lodsve.core.utils.StringUtils;
import lodsve.mongodb.Constants;
import lodsve.mongodb.connection.DynamicMongoConnection;
import lodsve.mongodb.core.MongoDataSourceBeanDefinitionFactory;
import lodsve.mongodb.repository.LodsveAnnotationRepositoryConfigurationSource;
import lodsve.mongodb.repository.LodsveMongoRepositoryConfigurationExtension;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.context.MappingContextIsNewStrategyFactory;
import org.springframework.data.mongodb.CannotGetMongoDbConnectionException;
import org.springframework.data.mongodb.config.WriteConcernPropertyEditor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * 加载mongodb操作的一些bean.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午10:15
 */
public class MongoBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;

    private final Map<String, BeanDefinition> BEAN_DEFINITION_MAP = new HashMap<>(16);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMongo.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMongo.class.getName(), metadata.getClassName()));

        String[] domainPackage = attributes.getStringArray(Constants.DOMAIN_PACKAGES_ATTRIBUTE_NAME);
        if (ArrayUtils.isEmpty(domainPackage)) {
            domainPackage = findDefaultPackage(metadata);
        }

        String[] basePackages = attributes.getStringArray(Constants.BASE_PACKAGES_ATTRIBUTE_NAME);
        if (ArrayUtils.isEmpty(basePackages)) {
            domainPackage = findDefaultPackage(metadata);
        }

        initMongoDataSource(attributes);
        initMongoMappingContext(domainPackage);
        initMappingContextIsNewStrategyFactory();
        initMappingConverter();
        initMongoPersistentEntityIndexCreator();
        initMongoTemplate();
        initMongoRepositoryFactory();
        initMongoRepository(metadata, registry);

        BeanRegisterUtils.registerBeans(BEAN_DEFINITION_MAP, registry);
    }

    private void initMongoRepository(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationRepositoryConfigurationSource configurationSource = new LodsveAnnotationRepositoryConfigurationSource(annotationMetadata, EnableMongo.class, resourceLoader, environment, registry);
        RepositoryConfigurationExtension extension = new LodsveMongoRepositoryConfigurationExtension(Constants.MONGO_TEMPLATE_BEAN_NAME);
        RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);

        RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, resourceLoader, environment);
        delegate.registerRepositoriesIn(registry, extension);
    }

    private void initMongoDataSource(AnnotationAttributes attributes) {
        String[] dataSources = attributes.getStringArray(Constants.DATA_SOURCE_ATTRIBUTE_NAME);
        if (dataSources.length == 0) {
            throw new CannotGetMongoDbConnectionException("can't find any datasource!");
        }

        String defaultMongoURIBeanName = "";
        List<String> mongoURIBeanNames = new ArrayList<>(dataSources.length);
        for (int i = 0; i < dataSources.length; i++) {
            String name = dataSources[i];
            BeanDefinition dsBeanDefinition = new MongoDataSourceBeanDefinitionFactory(name).build();

            if (i == 0) {
                defaultMongoURIBeanName = name;
            }

            BEAN_DEFINITION_MAP.put(name, dsBeanDefinition);
            mongoURIBeanNames.add(name);
        }

        BeanDefinitionBuilder dynamicMongoConnection = BeanDefinitionBuilder.genericBeanDefinition(DynamicMongoConnection.class);
        dynamicMongoConnection.addConstructorArgValue(mongoURIBeanNames);
        dynamicMongoConnection.addConstructorArgValue(defaultMongoURIBeanName);

        BEAN_DEFINITION_MAP.put(Constants.DATA_SOURCE_BEAN_NAME, dynamicMongoConnection.getBeanDefinition());
    }

    private void initMongoRepositoryFactory() {
        BeanDefinitionBuilder mongoRepositoryFactory = BeanDefinitionBuilder.genericBeanDefinition(MongoRepositoryFactory.class);
        mongoRepositoryFactory.addConstructorArgReference(Constants.MONGO_TEMPLATE_BEAN_NAME);

        BEAN_DEFINITION_MAP.put(Constants.MONGO_REPOSITORY_FACTORY_BEAN_NAME, mongoRepositoryFactory.getBeanDefinition());
    }

    private void initMappingConverter() {
        BeanDefinitionBuilder converterBuilder = BeanDefinitionBuilder.genericBeanDefinition(MappingMongoConverter.class);
        converterBuilder.addConstructorArgReference(Constants.DATA_SOURCE_BEAN_NAME);
        converterBuilder.addConstructorArgReference(Constants.MONGO_MAPPING_CONTEXT_BEAN_NAME);
        converterBuilder.addPropertyReference("typeMapper", Constants.DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME);

        BEAN_DEFINITION_MAP.put(Constants.MAPPING_CONVERTER_BEAN_NAME, converterBuilder.getBeanDefinition());
    }

    private void initMongoMappingContext(String[] domainPackage) {
        BeanDefinitionBuilder mappingContextBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoMappingContext.class);
        Set<String> classesToAdd = getInititalEntityClasses(domainPackage);
        if (classesToAdd != null) {
            mappingContextBuilder.addPropertyValue("initialEntitySet", classesToAdd);
        }

        BEAN_DEFINITION_MAP.put(Constants.MONGO_MAPPING_CONTEXT_BEAN_NAME, mappingContextBuilder.getBeanDefinition());
    }

    private void initMongoPersistentEntityIndexCreator() {
        BeanDefinitionBuilder indexHelperBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoPersistentEntityIndexCreator.class);
        indexHelperBuilder.addConstructorArgReference(Constants.MONGO_MAPPING_CONTEXT_BEAN_NAME);
        indexHelperBuilder.addConstructorArgReference(Constants.DATA_SOURCE_BEAN_NAME);
        indexHelperBuilder.addDependsOn(Constants.MONGO_MAPPING_CONTEXT_BEAN_NAME);

        BEAN_DEFINITION_MAP.put(Constants.INDEX_HELPER_BEAN_NAME, indexHelperBuilder.getBeanDefinition());
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

    private void initMappingContextIsNewStrategyFactory() {
        BeanDefinitionBuilder mappingContextStrategyFactoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(MappingContextIsNewStrategyFactory.class);
        mappingContextStrategyFactoryBuilder.addConstructorArgReference(Constants.MONGO_MAPPING_CONTEXT_BEAN_NAME);

        BEAN_DEFINITION_MAP.put(Constants.IS_NEW_STRATEGY_FACTORY_BEAN_NAME, mappingContextStrategyFactoryBuilder.getBeanDefinition());
    }

    private void initMongoTemplate() {
        BeanDefinitionBuilder mongoTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoTemplate.class);
        mongoTemplateBuilder.addConstructorArgReference(Constants.DATA_SOURCE_BEAN_NAME);
        mongoTemplateBuilder.addConstructorArgReference(Constants.MAPPING_CONVERTER_BEAN_NAME);

        BeanDefinitionBuilder writeConcernPropertyEditorBuilder = getWriteConcernPropertyEditorBuilder();

        BEAN_DEFINITION_MAP.put(Constants.CUSTOM_EDITOR_CONFIGURER_BEAN_NAME, writeConcernPropertyEditorBuilder.getBeanDefinition());
        BEAN_DEFINITION_MAP.put(Constants.MONGO_TEMPLATE_BEAN_NAME, mongoTemplateBuilder.getBeanDefinition());
    }

    private BeanDefinitionBuilder getWriteConcernPropertyEditorBuilder() {
        Map<String, Class<?>> customEditors = new ManagedMap<>();
        customEditors.put("com.mongodb.WriteConcern", WriteConcernPropertyEditor.class);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CustomEditorConfigurer.class);
        builder.addPropertyValue("customEditors", customEditors);

        return builder;
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

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
