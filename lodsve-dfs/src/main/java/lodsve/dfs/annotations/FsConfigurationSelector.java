package lodsve.dfs.annotations;

import lodsve.dfs.configuration.FsBeanDefinitionRegistrar;
import lodsve.dfs.configuration.FsConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 加载配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 11:19
 */
public class FsConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{FsConfiguration.class.getName(), FsBeanDefinitionRegistrar.class.getName()};
    }
}
