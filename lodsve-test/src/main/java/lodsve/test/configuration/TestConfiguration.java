/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.test.configuration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.thimbleware.jmemcached.Cache;
import lodsve.core.condition.ConditionalOnClass;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    public static class Mockito {
    }

    @Configuration
    @ComponentScan("lodsve.test.mock.mockserver")
    @ConditionalOnClass(ClientAndServer.class)
    public static class MockServer {
    }

    @Configuration
    @ComponentScan("lodsve.test.mock.dbunit")
    @ConditionalOnClass(DbUnitTestExecutionListener.class)
    public static class Dbunit {
    }

    @Configuration
    @ComponentScan("lodsve.test.mock.powermock")
    @ConditionalOnClass(PowerMockRunner.class)
    public static class PowerMock {
    }

    @Configuration
    @ComponentScan("lodsve.test.mock.memcached")
    @ConditionalOnClass(Cache.class)
    public static class MemcachedMock {
    }
}
