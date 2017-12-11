package lodsve.mongodb.annotations;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

/**
 * Lodsve custom MongoRepositoryConfigurationExtension.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-11-0011 14:24
 */
public class LodsveMongoRepositoryConfigurationExtension extends MongoRepositoryConfigurationExtension {
    private String mongoTemplateId;

    public LodsveMongoRepositoryConfigurationExtension(String mongoTemplateId) {
        super();
        this.mongoTemplateId = mongoTemplateId;
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        builder.addPropertyReference("mongoOperations", mongoTemplateId);
        builder.addPropertyValue("createIndexesForQueryMethods", false);
    }

    @Override
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
        super.registerBeansForRoot(registry, configurationSource);
    }
}
