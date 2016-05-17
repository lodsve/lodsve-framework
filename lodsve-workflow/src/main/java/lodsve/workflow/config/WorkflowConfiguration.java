package lodsve.workflow.config;

import lodsve.mybatis.configs.annotations.EnableMyBatis;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 工作流JavaConfig.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/19 下午4:18
 */
@Configuration
@EnableMyBatis(
        dataSource = "workflow",
        basePackages = "message.workflow.repository",
        enumsLocations = "message.workflow.enums",
        useFlyway = true,
        migration = "db/workflow"
)
@ComponentScan("lodsve.workflow")
public class WorkflowConfiguration {
}
