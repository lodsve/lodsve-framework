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
package lodsve.core.io;

import lodsve.core.utils.ListUtils;
import lodsve.core.utils.StringUtils;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.core.io.AbstractResource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zookeeper Resource.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 12:58
 */
public class ZookeeperResource extends AbstractResource {
    private static final Map<String, ZkClient> ZK_CLIENTS = new HashMap<>(16);

    private static final String URL_SEPARATOR = "/";

    private final String server;
    private final String path;

    /**
     * url like '127.0.0.1:2181/path1/path2/path3'
     *
     * @param url url
     */
    public ZookeeperResource(String url) {
        Assert.notNull(url, "Url must not be null");

        String[] paths = StringUtils.split(url, URL_SEPARATOR);

        Assert.isTrue(paths.length >= 2, "Url must be like '127.0.0.1:2181/path1/path2/path3'");
        this.server = paths[0];
        String path = StringUtils.substringAfter(url, paths[0]);
        path = path.endsWith(URL_SEPARATOR) ? StringUtils.substring(path, 0, path.length() - 1) : path;
        this.path = org.springframework.util.StringUtils.cleanPath(path);
    }

    /**
     * url like '127.0.0.1:2181'
     * path like '/path1/path2/path3'
     *
     * @param url  url
     * @param path path
     */
    public ZookeeperResource(String url, String path) {
        Assert.notNull(url, "Url must not be null");
        Assert.notNull(path, "Path must not be null");

        this.server = url;
        path = StringUtils.startsWith(path, URL_SEPARATOR) ? path : URL_SEPARATOR + path;
        path = StringUtils.endsWith(path, URL_SEPARATOR) ? StringUtils.substring(path, 0, path.length() - 1) : path;
        this.path = org.springframework.util.StringUtils.cleanPath(path);
    }

    @Override
    public String getDescription() {
        return "zookeeper resource [" + server + path + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return createSession().readData(path);
    }

    @Override
    public boolean exists() {
        return createSession().exists(path);
    }

    @Override
    public String getFilename() {
        String[] paths = StringUtils.split(path, "/");
        return paths.length < 1 ? path : paths[paths.length - 1];
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    public List<String> listChildren() {
        return createSession().getChildren(path);
    }

    public List<String> listChildren(final String... filter) {
        return ListUtils.findMore(createSession().getChildren(path), new ListUtils.Decide<String>() {
            @Override
            public boolean judge(String target) {
                for (String f : filter) {
                    if (target.endsWith(f)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public boolean isFolder() {
        List<String> children = createSession().getChildren(path);
        return !CollectionUtils.isEmpty(children);
    }

    public String getPath() {
        return server + path;
    }

    private ZkClient createSession() {
        if (ZK_CLIENTS.containsKey(server)) {
            return ZK_CLIENTS.get(server);
        }

        ZkClient client = new ZkClient(server, 1000, 1000, new InputStreamSerializer());
        ZK_CLIENTS.put(server, client);

        return client;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
