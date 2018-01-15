package lodsve.mybatis.configs;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.mybatis.properties.P6spyProperties;
import lodsve.mybatis.properties.RdbmsProperties;
import lodsve.mybatis.type.TypeHandlerScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午10:21
 */
@Configuration
@EnableConfigurationProperties({RdbmsProperties.class, P6spyProperties.class})
@ComponentScan({"lodsve.mybatis.key", "lodsve.mybatis.datasource"})
@EnableAspectJAutoProxy
public class MyBatisConfiguration {
    @Bean
    public TypeHandlerScanner typeHandlerScanner() {
        return new TypeHandlerScanner();
    }
}
