/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package message.test.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

/**
 * 使用mockserver的基类.<br/>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16-3-10 12:34
 */
@PowerMockIgnore("javax.net.ssl.*")
public class MockServerBaseTest extends PowerMockitoBaseTest {
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
        if (mockServer != null)
            mockServer.stop();
        
        baseUrl = "";
    }
}
