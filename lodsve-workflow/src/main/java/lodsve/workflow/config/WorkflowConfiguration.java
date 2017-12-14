package lodsve.workflow.config;

import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.datasource.annotations.DataSource;
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
        dataSource = @DataSource("workflow"),
        basePackages = "lodsve.workflow.repository",
        enumsLocations = "lodsve.workflow.enums"
)
@ComponentScan("lodsve.workflow")
public class WorkflowConfiguration {
}
