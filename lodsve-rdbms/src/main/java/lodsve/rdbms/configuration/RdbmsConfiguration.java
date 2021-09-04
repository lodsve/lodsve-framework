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
package lodsve.rdbms.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.condition.ConditionalOnWebApplication;
import lodsve.core.utils.StringUtils;
import lodsve.rdbms.Constants;
import lodsve.rdbms.druid.DruidInitializer;
import lodsve.rdbms.dynamic.DynamicDataSourceAspect;
import lodsve.rdbms.flyway.FlywayMigrationInitializer;
import lodsve.rdbms.flyway.FlywayMigrationStrategy;
import lodsve.rdbms.properties.DruidProperties;
import lodsve.rdbms.properties.FlywayProperties;
import lodsve.rdbms.properties.P6SpyProperties;
import lodsve.rdbms.properties.RdbmsProperties;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 关系型数据库数据源配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Configuration
@EnableConfigurationProperties({RdbmsProperties.class, P6SpyProperties.class, DruidProperties.class, FlywayProperties.class})
@ComponentScan("lodsve.rdbms.dynamic")
@EnableAspectJAutoProxy
public class RdbmsConfiguration {
    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean
    @ConditionalOnClass(StatViewServlet.class)
    @ConditionalOnWebApplication
    public DruidInitializer druidInitializer(ObjectProvider<DruidProperties> druidProperties) {
        DruidInitializer initializer = new DruidInitializer();
        initializer.setDruidProperties(druidProperties.getIfAvailable());
        return initializer;
    }

    @Configuration
    @ConditionalOnClass(Flyway.class)
    @Profile("flyway")
    public class FlywayConfiguration {
        private final static String DEFAULT_FLYWAY_LOCATION = "classpath:META-INF/flyway";

        private final FlywayMigrationStrategy migrationStrategy;

        @Autowired
        public FlywayConfiguration(ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
            this.migrationStrategy = migrationStrategy.getIfAvailable();
        }

        @Bean(name = Constants.FLYWAY_BEAN_NAME)
        public List<Flyway> flyway(ObjectProvider<Map<String, DataSource>> dataSourcesProvider, ObjectProvider<FlywayProperties> flywayPropertiesProvider) {
            Map<String, DataSource> dataSources = dataSourcesProvider.getIfAvailable();
            FlywayProperties flywayProperties = flywayPropertiesProvider.getIfAvailable();
            if (null == dataSources || null == flywayProperties) {
                return Lists.newArrayList();
            }

            // 要排除  p6spy的"lodsveRealDataSource"数据源
            // 还要排除默认数据源 "lodsveDataSource"
            List<Flyway> flyways = Lists.newArrayList();
            dataSources.keySet().stream().filter(k -> !Lists.newArrayList(Constants.DATA_SOURCE_BEAN_NAME, Constants.REAL_DATA_SOURCE_BEAN_NAME).contains(k)).forEach(k -> {
                Flyway flyway = new Flyway();
                flyway.setDataSource(dataSources.get(k));
                String location = flywayProperties.getLocations().get(k);
                flyway.setLocations(StringUtils.isBlank(location) ? DEFAULT_FLYWAY_LOCATION : location);

                flyways.add(flyway);
            });

            return flyways;
        }

        @Bean
        @ConditionalOnMissingBean
        public FlywayMigrationInitializer flywayInitializer(List<Flyway> flyways) {
            return new FlywayMigrationInitializer(flyways, this.migrationStrategy);
        }
    }
}
