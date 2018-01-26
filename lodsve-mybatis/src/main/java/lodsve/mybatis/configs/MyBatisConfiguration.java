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

import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.mybatis.enums.DbType;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.properties.P6SpyProperties;
import lodsve.mybatis.properties.RdbmsProperties;
import lodsve.mybatis.type.TypeHandlerScanner;
import lodsve.mybatis.utils.Constants;
import lodsve.mybatis.utils.MyBatisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/19 下午10:21
 */
@Configuration
@EnableConfigurationProperties({RdbmsProperties.class, P6SpyProperties.class})
@ComponentScan({"lodsve.mybatis.key", "lodsve.mybatis.datasource"})
@EnableAspectJAutoProxy
public class MyBatisConfiguration {
    @Autowired
    @Qualifier(Constants.DATA_SOURCE_BEAN_NAME)
    private DataSource dataSource;

    @Bean
    public TypeHandlerScanner typeHandlerScanner() {
        return new TypeHandlerScanner();
    }

    @Bean(name = Constants.ID_GENERATOR_BANE_NAME)
    public IDGenerator idGenerator() {
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
}
