/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/12 13:07
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
