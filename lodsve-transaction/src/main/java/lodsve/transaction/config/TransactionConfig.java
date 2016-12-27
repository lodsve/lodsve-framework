package lodsve.transaction.config;

import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.transaction.utils.PingppProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 14:27
 */
@Configuration
@EnableMyBatis(
        dataSource = "transaction",
        basePackages = "lodsve.transaction.repository",
        enumsLocations = "lodsve.transaction.enums",
        useFlyway = true,
        migration = "db/transaction"
)
@ComponentScan(basePackages = {"lodsve.transaction.action", "lodsve.transaction.channel", "lodsve.transaction.repository"})
@EnableConfigurationProperties(PingppProperties.class)
public class TransactionConfig {
}
