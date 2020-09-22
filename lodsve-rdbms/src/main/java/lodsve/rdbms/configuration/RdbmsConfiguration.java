/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
