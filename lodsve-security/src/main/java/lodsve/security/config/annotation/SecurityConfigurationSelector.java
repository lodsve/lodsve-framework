package lodsve.security.config.annotation;

import lodsve.security.config.SecurityConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载security的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/8 上午11:24
 */
public class SecurityConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(SecurityConfig.class.getName());

        return imports.toArray(new String[imports.size()]);
    }
}
