package message.transaction.config;

import message.mybatis.configs.annotations.EnableMyBatis;
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
        basePackages = "message.transaction.repository",
        enumsLocations = "message.transaction.enums",
        useFlyway = true,
        migration = "db/transaction"
)
@ComponentScan(basePackages = {"message.transaction.action", "message.transaction.channel", "message.transaction.repository"})
public class TransactionConfig {
}
