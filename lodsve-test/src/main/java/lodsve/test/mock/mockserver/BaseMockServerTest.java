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
package lodsve.test.mock.mockserver;

import lodsve.test.mock.powermock.BasePowerMockitoTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

/**
 * 使用mockserver的基类.<br/>
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16-3-10 12:34
 */
@PowerMockIgnore("javax.net.ssl.*")
public class BaseMockServerTest extends BasePowerMockitoTest {
    protected static ClientAndServer mockServer;
    /**
     * 基础url，如http://localhost:8080
     */
    protected static String baseUrl;

    @BeforeClass
    public static void startServer() throws Exception {
        int port = PortFactory.findFreePort();
        System.out.println("port is " + port);
        mockServer = ClientAndServer.startClientAndServer(port);
        baseUrl = "http://localhost:" + port;
    }

    @AfterClass
    public static void stopServer() {
        if (mockServer != null) {
            mockServer.stop();
        }

        baseUrl = "";
    }
}
