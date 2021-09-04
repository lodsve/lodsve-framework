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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * 使用spring mvc实现restful客户端.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-3-26 14:44
 */
public class RestUtils {
    private static RestTemplate restTemplate;

    public static void setRestTemplate(RestTemplate restTemplate) {
        RestUtils.restTemplate = restTemplate;
    }

    // GET

    public static <T> T get(URI url, Class<T> responseType) {
        Assert.notNull(url);
        Assert.notNull(responseType);

        return restTemplate.getForObject(url, responseType);
    }

    public static <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        Assert.hasText(url);

        return get(expand(url, uriVariables), responseType);
    }

    public static <T> T get(String url, Class<T> responseType) {
        return get(url, responseType, new Object[0]);
    }

    public static <T> T get(String url, Class<T> responseType, Map<String, ?> urlVariables) {
        Assert.hasText(url);

        return get(expand(url, urlVariables), responseType);
    }

    // POST

    public static <T> T post(URI url, Object request, Class<T> responseType) {
        Assert.notNull(url);
        Assert.notNull(responseType);

        return restTemplate.postForObject(url, request, responseType);
    }

    public static <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        Assert.hasText(url);

        return post(expand(url, uriVariables), request, responseType);
    }

    public static <T> T post(String url, Object request, Class<T> responseType) {
        return post(url, request, responseType, new Object[0]);
    }

    public static <T> T post(String url, Object request, Class<T> responseType, Map<String, ?> urlVariables) {
        Assert.hasText(url);

        return post(expand(url, urlVariables), request, responseType);
    }

    // HEAD

    public static HttpHeaders head(String url, Object... urlVariables) throws RestClientException {
        return head(expand(url, urlVariables));
    }

    public static HttpHeaders head(String url, Map<String, ?> urlVariables) throws RestClientException {
        return head(expand(url, urlVariables));
    }

    public static HttpHeaders head(URI url) throws RestClientException {
        return restTemplate.headForHeaders(url);
    }

    // PUT

    public static void put(String url, Object request, Object... urlVariables) throws RestClientException {
        put(expand(url, urlVariables), request);

    }

    public static void put(String url, Object request, Map<String, ?> urlVariables) throws RestClientException {
        put(expand(url, urlVariables), request);
    }

    public static void put(URI url, Object request) throws RestClientException {
        restTemplate.put(url, request);
    }

    // DELETE

    public static void delete(String url, Object... urlVariables) throws RestClientException {
        delete(expand(url, urlVariables));
    }

    public static void delete(String url, Map<String, ?> urlVariables) throws RestClientException {
        delete(expand(url, urlVariables));
    }

    public static void delete(URI url) throws RestClientException {
        restTemplate.delete(url);
    }

    // OPTIONS

    public static Set<HttpMethod> options(String url, Object... urlVariables) throws RestClientException {
        return options(expand(url, urlVariables));
    }

    public static Set<HttpMethod> options(String url, Map<String, ?> urlVariables) throws RestClientException {
        return options(expand(url, urlVariables));
    }

    public static Set<HttpMethod> options(URI url) throws RestClientException {
        return restTemplate.optionsForAllow(url);
    }

    // COMMONS

    private static URI expand(String url, Object... uriVariables) {
        Assert.hasText(url);
        return restTemplate.getUriTemplateHandler().expand(url, uriVariables);
    }

    private static URI expand(String url, Map<String, ?> uriVariables) {
        Assert.hasText(url);
        return restTemplate.getUriTemplateHandler().expand(url, uriVariables);
    }
}
