package lodsve.workflow.config.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用workflow.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/8 上午10:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WorkflowConfigurationSelector.class)
public @interface EnableWorkflow {
}
