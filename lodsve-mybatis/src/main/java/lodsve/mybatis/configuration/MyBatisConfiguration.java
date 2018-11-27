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

package lodsve.mybatis.configuration;

import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.io.support.LodsveResourceLoader;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.properties.MyBatisProperties;
import lodsve.mybatis.type.TypeHandlerScanner;
import lodsve.mybatis.utils.Constants;
import lodsve.mybatis.utils.DbType;
import lodsve.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/19 下午10:21
 */
@Configuration
@EnableConfigurationProperties(MyBatisProperties.class)
@EnableAspectJAutoProxy
public class MyBatisConfiguration {
    private MyBatisProperties myBatisProperties;

    public MyBatisConfiguration() {
        this.myBatisProperties = new RelaxedBindFactory.Builder<>(MyBatisProperties.class).build();
    }

    @Bean(name = Constants.ID_GENERATOR_BANE_NAME)
    public IDGenerator idGenerator(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) {
        DbType type = MyBatisUtils.getDbType(dataSource);
        switch (type) {
            case DB_MYSQL:
                MySQLIDGenerator mySQLIDGenerator = new MySQLIDGenerator();
                mySQLIDGenerator.setDataSource(dataSource);
                mySQLIDGenerator.setCacheSize(myBatisProperties.getKeyCacheSize());
                return mySQLIDGenerator;
            case DB_ORACLE:
                OracleIDGenerator oracleIDGenerator = new OracleIDGenerator();
                oracleIDGenerator.setDataSource(dataSource);
                return oracleIDGenerator;
            default:
                return new SnowflakeIdGenerator();
        }
    }

    @Bean(name = Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME)
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(getResources(myBatisProperties.getMapperLocations()));
        sqlSessionFactoryBean.setConfigLocation(new LodsveResourceLoader().getResource(myBatisProperties.getConfigLocation()));
        sqlSessionFactoryBean.setTypeHandlers(new TypeHandlerScanner().find(StringUtils.join(myBatisProperties.getEnumsLocations(), ",")));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = Constants.MAPPER_SCANNER_CONFIGURER_BANE_NAME)
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(StringUtils.join(myBatisProperties.getBasePackages(), ","));
        mapperScannerConfigurer.setAnnotationClass(Repository.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME);

        return mapperScannerConfigurer;
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
