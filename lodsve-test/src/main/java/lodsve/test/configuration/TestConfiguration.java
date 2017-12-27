package lodsve.test.configuration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import lodsve.core.condition.ConditionalOnClass;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Test Configuration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 13:07
 */
@Configuration
public class TestConfiguration {
    @Configuration
    @ComponentScan("lodsve.test.mock.mockito")
    @ConditionalOnClass(MockitoAnnotations.class)
    public static class Mockito{}

    @Configuration
    @ComponentScan("lodsve.test.mock.mockserver")
    @ConditionalOnClass(ClientAndServer.class)
    public static class MockServer{}

    @Configuration
    @ComponentScan("lodsve.test.mock.dbunit")
    @ConditionalOnClass(DbUnitTestExecutionListener.class)
    public static class Dbunit{}

    @Configuration
    @ComponentScan("lodsve.test.mock.powermock")
    @ConditionalOnClass(PowerMockRunner.class)
    public static class PowerMock{}
}
