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

package lodsve.mongodb.connection;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.CannotGetMongoDbConnectionException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoDbUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dynamic mongo db datasource.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午6:15
 */
public class DynamicMongoConnection implements DisposableBean, MongoDbFactory, BeanFactoryAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DynamicMongoConnection.class);
    private List<Mongo> activityMongos;
    private Map<String, MongoClientURI> mongoURIs;
    private MongoClientURI defaultMongoURI;
    private BeanFactory beanFactory;

    private List<String> mongoURIBeanNames;
    private String defaultMongoURIBeanName;

    public DynamicMongoConnection(List<String> mongoURIBeanNames, String defaultMongoURIBeanName) {
        this.mongoURIBeanNames = mongoURIBeanNames;
        this.defaultMongoURIBeanName = defaultMongoURIBeanName;
    }

    private MongoClientURI getDataSourceByBeanName(String beanName) {
        Object object = beanFactory.getBean(beanName);
        if (object == null || !(object instanceof MongoClientURI)) {
            if (logger.isErrorEnabled()) {
                logger.error("The bean named '{}' is not a {}!", beanName, MongoClientURI.class.getName());
            }
            throw new CannotGetMongoDbConnectionException(String.format("The bean named '%s' is not a '%s'!", beanName, MongoClientURI.class.getName()));
        }

        return (MongoClientURI) object;
    }

    @Override
    public DB getDb() throws DataAccessException {
        MongoClientURI clientURI = determineTargetDataSource();

        return getDb(clientURI.getDatabase());
    }

    @Override
    public DB getDb(String dbName) throws DataAccessException {
        Assert.hasText(dbName, "Database name must not be empty.");
        MongoClientURI mongoURI = determineTargetDataSource();

        return MongoDbUtils.getDB(getMongo(mongoURI), dbName, new UserCredentials(mongoURI.getUsername(), parseChars(mongoURI.getPassword())), mongoURI.getDatabase());
    }

    private Mongo getMongo(MongoClientURI mongoURI) {
        try {
            Mongo mongo = new MongoClient(mongoURI);
            activityMongos.add(mongo);

            return mongo;
        } catch (UnknownHostException e) {
            throw new CannotGetMongoDbConnectionException(String.format("con't get mongo '%s' connection!", mongoURI.getHosts()));
        }
    }


    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return null;
    }

    private static String parseChars(char[] chars) {
        return chars == null ? null : String.valueOf(chars);
    }

    /**
     * 指定使用的mongouri key
     *
     * @return key
     */
    private MongoClientURI determineTargetDataSource() {
        String currentKey = MongoDataSourceHolder.get();
        MongoClientURI mongoURI;

        mongoURI = mongoURIs.get(currentKey);

        if (null == mongoURI) {
            mongoURI = defaultMongoURI;
        }

        if (null == mongoURI) {
            throw new CannotGetMongoDbConnectionException(String.format("determine current lookup key '%s' not exist!", currentKey));
        }

        return mongoURI;
    }

    @Override
    public void destroy() throws Exception {
        if (!CollectionUtils.isEmpty(activityMongos)) {
            for (Mongo mongo : activityMongos) {
                mongo.close();
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, MongoClientURI> targetDataSources = new HashMap<>(mongoURIBeanNames.size());
        for (String beanName : mongoURIBeanNames) {
            MongoClientURI clientURI = getDataSourceByBeanName(beanName);

            targetDataSources.put(beanName, clientURI);
        }
        if (StringUtils.isNotBlank(defaultMongoURIBeanName)) {
            defaultMongoURI = getDataSourceByBeanName(defaultMongoURIBeanName);
        }
        this.mongoURIs = targetDataSources;

        activityMongos = new ArrayList<>(mongoURIs.size());
    }
}
