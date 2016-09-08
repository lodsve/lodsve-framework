package lodsve.workflow.config.annotation;

import lodsve.workflow.config.WorkflowConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载workflow的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/8 上午10:54
 */
public class WorkflowConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(WorkflowConfiguration.class.getName());

        return imports.toArray(new String[imports.size()]);
    }
}
