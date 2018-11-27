/*
 * Copyright (C) 2018, All rights Reserved, Designed By www.xiniaoyun.com
 * @author: 孙昊
 * @date: 2018-11-27 17:35
 * @Copyright: 2018 www.xiniaoyun.com Inc. All rights reserved.
 * 注意：本内容仅限于南京微欧科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package lodsve.rdbms.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

/**
 * .
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
        DruidInitializer initializingBean = new DruidInitializer();
        initializingBean.setDruidProperties(druidProperties);

        return initializingBean;
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
        public Flyway flyway(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource, FlywayProperties flywayProperties) {
            Flyway flyway = new Flyway();
            flyway.setDataSource(dataSource);
            flyway.setLocations(flywayProperties.getLocations());

            return flyway;
        }

        @Bean
        @ConditionalOnMissingBean
        public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
            return new FlywayMigrationInitializer(flyway, this.migrationStrategy);
        }
    }
}
