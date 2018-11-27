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

import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.properties.MyBatisProperties;
import lodsve.mybatis.utils.DbType;
import lodsve.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/19 下午10:21
 */
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(MyBatisProperties.class)
public class MyBatisConfiguration {
    public static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    private MyBatisProperties myBatisProperties;
    private final List<ConfigurationCustomizer> customizers;

    public MyBatisConfiguration(ObjectProvider<List<ConfigurationCustomizer>> customizers) {
        this.customizers = customizers.getIfAvailable();
        this.myBatisProperties = new RelaxedBindFactory.Builder<>(MyBatisProperties.class).build();
    }

    @Bean
    public IDGenerator idGenerator(@Qualifier(DATA_SOURCE_BEAN_NAME) DataSource dataSource) {
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

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        Configuration configuration = new Configuration();
        customizers.forEach(c -> c.customize(configuration));
        factory.setConfiguration(configuration);

        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
