/*
 * Copyright (C) 2018, All rights Reserved, Designed By www.xiniaoyun.com
 * @author: 孙昊
 * @date: 2018-11-27 17:35
 * @Copyright: 2018 www.xiniaoyun.com Inc. All rights reserved.
 * 注意：本内容仅限于南京微欧科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package lodsve.rdbms.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.condition.ConditionalOnWebApplication;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
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
    public DruidInitializer druidInitializer(DruidProperties druidProperties) {
        DruidInitializer initializer = new DruidInitializer();
        initializer.setDruidProperties(druidProperties);
        return initializer;
    }

    @Configuration
    @ConditionalOnClass(Flyway.class)
    @Profile("flyway")
    public class FlywayConfiguration {
        private final FlywayMigrationStrategy migrationStrategy;

        @Autowired
        public FlywayConfiguration(ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
            this.migrationStrategy = migrationStrategy.getIfAvailable();
        }

        @Bean(name = Constants.FLYWAY_BEAN_NAME)
        public List<Flyway> flyway(List<DataSource> dataSources, FlywayProperties flywayProperties) {

            List<Flyway> flyways = Lists.newArrayList();
            dataSources.forEach(d -> {
                Flyway flyway = new Flyway();
                flyway.setDataSource(d);
                flyway.setLocations(flywayProperties.getLocations());

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
