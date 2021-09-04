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
package lodsve.mongodb.core;

import com.mongodb.MongoClientURI;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.mongodb.properties.MongoConnection;
import lodsve.mongodb.properties.MongoProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.StringUtils;

/**
 * mongo db datasource.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午6:15
 */
public class MongoDataSourceBeanDefinitionFactory {
    private static final String URL_PREFIX = "mongodb://";

    private final String dataSourceName;
    private final MongoProperties mongoProperties;

    public MongoDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        this.mongoProperties = new RelaxedBindFactory.Builder<>(MongoProperties.class).build();
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder mongoURIBean = BeanDefinitionBuilder.genericBeanDefinition(MongoClientURI.class);
        mongoURIBean.addConstructorArgValue(getMongoUri());

        return mongoURIBean.getBeanDefinition();
    }

    private String getMongoUri() {
        MongoConnection connection = mongoProperties.getProject().get(dataSourceName);

        String url = connection.getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("uri must not null");
        }
        if (!url.startsWith(URL_PREFIX)) {
            throw new IllegalArgumentException("uri needs to start with " + URL_PREFIX);
        }

        StringBuilder uriBuilder = new StringBuilder(URL_PREFIX);


        String username = connection.getUsername();
        String password = connection.getPassword();

        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            uriBuilder.append(username).append(":").append(password).append("@");
        }

        uriBuilder.append(url.substring(URL_PREFIX.length()));

        uriBuilder.append("?maxpoolsize=");
        if (connection.getMaxPoolSize() != 0) {
            uriBuilder.append(connection.getMaxPoolSize());
        } else {
            uriBuilder.append(mongoProperties.getMaxPoolSize());
        }

        return uriBuilder.toString();
    }
}
