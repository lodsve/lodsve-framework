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

package lodsve.mybatis.configs;

import com.alibaba.druid.support.http.StatViewServlet;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnProperty;
import lodsve.core.condition.ConditionalOnWebApplication;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.io.support.LodsveResourceLoader;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import lodsve.mybatis.datasource.dynamic.DynamicDataSourceAspect;
import lodsve.mybatis.druid.DruidInitializer;
import lodsve.mybatis.enums.DbType;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.properties.DruidProperties;
import lodsve.mybatis.properties.MyBatisProperties;
import lodsve.mybatis.properties.P6SpyProperties;
import lodsve.mybatis.properties.RdbmsProperties;
import lodsve.mybatis.type.TypeHandlerScanner;
import lodsve.mybatis.utils.Constants;
import lodsve.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/19 下午10:21
 */
@Configuration
@EnableConfigurationProperties({RdbmsProperties.class, P6SpyProperties.class, DruidProperties.class, MyBatisProperties.class})
@ComponentScan({"lodsve.mybatis.key", "lodsve.mybatis.datasource"})
@EnableAspectJAutoProxy
public class MyBatisConfiguration {
    private MyBatisProperties myBatisProperties;

    public MyBatisConfiguration() {
        this.myBatisProperties = new RelaxedBindFactory.Builder<>(MyBatisProperties.class).build();
    }

    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean(name = Constants.ID_GENERATOR_BANE_NAME)
    public IDGenerator idGenerator(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) {
        DbType type = MyBatisUtils.getDbType(dataSource);
        switch (type) {
            case DB_MYSQL:
                MySQLIDGenerator mySQLIDGenerator = new MySQLIDGenerator();
                mySQLIDGenerator.setDataSource(dataSource);
                return mySQLIDGenerator;
            case DB_ORACLE:
                OracleIDGenerator oracleIDGenerator = new OracleIDGenerator();
                oracleIDGenerator.setDataSource(dataSource);
                return oracleIDGenerator;
            default:
                return new SnowflakeIdGenerator();
        }
    }

    @Bean
    @ConditionalOnClass(StatViewServlet.class)
    @ConditionalOnWebApplication
    public DruidInitializer druidInitializer(DruidProperties druidProperties) {
        DruidInitializer initializingBean = new DruidInitializer();
        initializingBean.setDruidProperties(druidProperties);

        return initializingBean;
    }

    @Bean(name = Constants.FLYWAY_BEAN_NAME, initMethod = "migrate")
    @ConditionalOnProperty(clazz = MyBatisProperties.class, key = "enableFlyway", value = "true")
    public Flyway flyway(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(myBatisProperties.getMigration());

        return flyway;
    }

    @Bean(name = Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME)
    @DependsOn(Constants.FLYWAY_BEAN_NAME)
    @ConditionalOnProperty(clazz = MyBatisProperties.class, key = "enableFlyway", value = "true")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        return makeSqlSessionFactoryBean(dataSource).getObject();
    }

    @Bean(name = Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME)
    @ConditionalOnProperty(clazz = MyBatisProperties.class, key = "enableFlyway", value = "false")
    public SqlSessionFactory noFlywaySqlSessionFactoryBean(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        return makeSqlSessionFactoryBean(dataSource).getObject();
    }

    @Bean(name = Constants.MAPPER_SCANNER_CONFIGURER_BANE_NAME)
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(StringUtils.join(myBatisProperties.getBasePackages(), ","));
        mapperScannerConfigurer.setAnnotationClass(Repository.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME);

        return mapperScannerConfigurer;
    }

    private SqlSessionFactoryBean makeSqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(getResources(myBatisProperties.getMapperLocations()));
        sqlSessionFactoryBean.setConfigLocation(new LodsveResourceLoader().getResource(myBatisProperties.getConfigLocation()));
        sqlSessionFactoryBean.setTypeHandlers(new TypeHandlerScanner().find(StringUtils.join(myBatisProperties.getEnumsLocations(), ",")));

        return sqlSessionFactoryBean;
    }

    private Resource[] getResources(String[] locations) throws IOException {
        Assert.notNull(locations);

        List<Resource> resources = new ArrayList<>(16);
        for (String location : locations) {
            Collections.addAll(resources, new LodsvePathMatchingResourcePatternResolver().getResources(location));
        }
        return resources.toArray(new Resource[resources.size()]);
    }
}
