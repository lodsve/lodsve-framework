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

package lodsve.mongodb.core;

import com.mongodb.MongoClientURI;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
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

    private String dataSourceName;
    private MongoProperties mongoProperties;

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
            uriBuilder.append(username + ":" + password + "@");
        }

        uriBuilder.append(url.substring(URL_PREFIX.length()));

        uriBuilder.append("?maxpoolsize=");
        if (connection.getMaxpoolsize() != 0) {
            uriBuilder.append(connection.getMaxpoolsize());
        } else {
            uriBuilder.append(mongoProperties.getMaxpoolsize());
        }

        return uriBuilder.toString();
    }
}
