package lodsve.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Lodsve Ext ConfigurationLoader.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 12:53
 */
public class LodsveExtConfigurationLoader implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> configs = new ArrayList<>(16);
        configs.addAll(getConfigurations());

        return configs.toArray(new String[configs.size()]);
    }

    private List<String> getConfigurations() {
        return SpringFactoriesLoader.loadFactoryNames(Configuration.class, Thread.currentThread().getContextClassLoader());
    }
}
