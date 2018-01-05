package lodsve.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * Lodsve Ext ConfigurationLoader.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 12:53
 */
public class LodsveExtConfigurationLoader implements ImportSelector {
    private static boolean isInit = false;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (isInit) {
            return new String[0];
        }

        isInit = true;
        List<String> imports = getConfigurations();
        return imports.toArray(new String[imports.size()]);
    }

    private List<String> getConfigurations() {
        return SpringFactoriesLoader.loadFactoryNames(Configuration.class, Thread.currentThread().getContextClassLoader());
    }
}
