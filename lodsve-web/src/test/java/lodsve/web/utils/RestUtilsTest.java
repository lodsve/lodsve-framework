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
package lodsve.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.web.mvc.config.WebMvcConfiguration;
import lodsve.web.utils.domain.Demo;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-3-26-0026 15:19
 */
public class RestUtilsTest {
    private static ClientAndServer mockServer;
    private static String baseUrl;

    @BeforeClass
    public static void startServer() {
        int port = PortFactory.findFreePort();
        mockServer = ClientAndServer.startClientAndServer(port);
        baseUrl = "http://localhost:" + port;

        mockServer.when(HttpRequest.request().withPath("/demo/1")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250")).withBody("{\"pkId\":1,\"userName\":\"孙昊\",\"sex\":{\"code\":\"0\",\"title\":\"性别不详\"}}"));

        mockServer.when(HttpRequest.request().withPath("/demo/void")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250")));

        mockServer.when(HttpRequest.request().withPath("/demo/save").withMethod("POST")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250")).withBody("2"));

        mockServer.when(HttpRequest.request().withPath("/demo/head/12").withMethod("HEAD")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250"), new Header("result", "successed")));

        mockServer.when(HttpRequest.request().withPath("/demo/put").withMethod("PUT").withBody("{\"pkId\":1,\"userName\":\"sunhao\"}")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250")));

        mockServer.when(HttpRequest.request().withPath("/demo/options").withMethod("OPTIONS")).respond(
            HttpResponse
                .response()
                .withHeaders(new Header("Content-Type", "application/json;charset=UTF-8"),
                    new Header("Content-Language", "zh-CN"), new Header("Server", "Apache-Coyote/1.1"),
                    new Header("Content-Length", "250"), new Header("Allow", "POST")));

        WebMvcConfiguration configuration = new WebMvcConfiguration();
        ObjectMapper objectMapper = configuration.objectMapper();
        RestTemplate restTemplate = configuration.restTemplate(objectMapper);
        RestUtils.setRestTemplate(restTemplate);
    }

    @AfterClass
    public static void stopServer() {
        if (mockServer != null) {
            mockServer.stop();
        }

        baseUrl = "";
    }

    /**
     * @see RestUtils#get(URI, Class)
     */
    @Test
    public void get() throws URISyntaxException {
        // 测试具体返回值
        URI uri = new URI(baseUrl + "/demo/1");
        Demo demo = RestUtils.get(uri, Demo.class);
        Assert.assertNotNull(demo);
    }

    /**
     * @see RestUtils#get(String, Class, Object...)
     */
    @Test
    public void get1() {
        Assert.assertNotNull(RestUtils.get(baseUrl + "/demo/{0}", Demo.class, 1L));
    }

    /**
     * @see RestUtils#get(String, Class)
     */
    @Test
    public void get2() {
        // 测试void
        Assert.assertNull(RestUtils.get(baseUrl + "/demo/void", Void.class));
    }

    /**
     * @see RestUtils#get(String, Class, Map)
     */
    @Test
    public void get3() {
        Assert.assertNotNull(RestUtils.get(baseUrl + "/demo/{pkId}", Demo.class, Collections.singletonMap("pkId", 1L)));
    }

    /**
     * @see RestUtils#post(URI, Object, Class)
     */
    @Test
    public void post() throws URISyntaxException {
        Long id = RestUtils.post(new URI(baseUrl + "/demo/save"), new Demo(2L, "sunhao"), Long.class);
        Assert.assertEquals(2L, id.longValue());
    }

    /**
     * @see RestUtils#post(String, Object, Class, Object...)
     */
    @Test
    public void post1() {
        Long id = RestUtils.post(baseUrl + "/demo/save", new Demo(2L, "sunhao"), Long.class, new Object[0]);
        Assert.assertEquals(2L, id.longValue());
    }

    /**
     * @see RestUtils#post(String, Object, Class)
     */
    @Test
    public void post2() {
        Long id = RestUtils.post(baseUrl + "/demo/save", new Demo(2L, "sunhao"), Long.class);
        Assert.assertEquals(2L, id.longValue());
    }

    /**
     * @see RestUtils#post(String, Object, Class, Map)
     */
    @Test
    public void post3() {
        Long id = RestUtils.post(baseUrl + "/demo/save", new Demo(2L, "sunhao"), Long.class, Collections.emptyMap());
        Assert.assertEquals(2L, id.longValue());
    }

    /**
     * @see RestUtils#head(String, Object...)
     */
    @Test
    public void head() {
        HttpHeaders httpHeaders = RestUtils.head(baseUrl + "/demo/head/{0}", 12L);
        Assert.assertEquals("successed", httpHeaders.getFirst("result"));
    }

    /**
     * @see RestUtils#head(String, Map)
     */
    @Test
    public void head1() {
        HttpHeaders httpHeaders = RestUtils.head(baseUrl + "/demo/head/{pkId}", Collections.singletonMap("pkId", 12L));
        Assert.assertEquals("successed", httpHeaders.getFirst("result"));
    }

    /**
     * @see RestUtils#head(URI)
     */
    @Test
    public void head2() throws URISyntaxException {
        HttpHeaders httpHeaders = RestUtils.head(new URI(baseUrl + "/demo/head/12"));
        Assert.assertEquals("successed", httpHeaders.getFirst("result"));
    }

    /**
     * @see RestUtils#put(String, Object, Object...)
     */
    @Test
    public void put() {
        RestUtils.put(baseUrl + "/demo/put", new Demo(1L, "sunhao"));
    }

    /**
     * @see RestUtils#put(String, Object, Map)
     */
    @Test
    public void put1() {
        RestUtils.put(baseUrl + "/demo/put", new Demo(1L, "sunhao"), Collections.emptyMap());
    }

    /**
     * @see RestUtils#put(URI, Object)
     */
    @Test
    public void put2() throws URISyntaxException {
        RestUtils.put(new URI(baseUrl + "/demo/put"), new Demo(1L, "sunhao"));
    }

    /**
     * @see RestUtils#delete(String, Object...)
     */
    @Test
    public void delete() {
    }

    /**
     * @see RestUtils#delete(String, Map)
     */
    @Test
    public void delete1() {
    }

    /**
     * @see RestUtils#delete(URI)
     */
    @Test
    public void delete2() {
    }

    /**
     * @see RestUtils#options(String, Object...)
     */
    @Test
    public void options() {
        Set<HttpMethod> methods = RestUtils.options(baseUrl + "/demo/options");
        Assert.assertTrue(methods.contains(HttpMethod.POST));
    }

    /**
     * @see RestUtils#options(String, Map)
     */
    @Test
    public void options1() {
        Set<HttpMethod> methods = RestUtils.options(baseUrl + "/demo/options", Collections.emptyMap());
        Assert.assertTrue(methods.contains(HttpMethod.POST));
    }

    /**
     * @see RestUtils#options(URI)
     */
    @Test
    public void options2() throws URISyntaxException {
        Set<HttpMethod> methods = RestUtils.options(new URI(baseUrl + "/demo/options"));
        Assert.assertTrue(methods.contains(HttpMethod.POST));
    }
}
