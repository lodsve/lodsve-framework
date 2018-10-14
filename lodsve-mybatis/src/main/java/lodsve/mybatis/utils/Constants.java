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

package lodsve.mybatis.utils;

/**
 * 常量.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/15 下午11:11
 */
public class Constants {
    private Constants() {
    }

    public static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";

    public static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    public static final String REAL_DATA_SOURCE_BEAN_NAME = "lodsveRealDataSource";
    public static final String FLYWAY_BEAN_NAME = "lodsveFlyway";
    public static final String MYBATIS_SQL_SESSION_FACTORY_BANE_NAME = "sqlSessionFactory";
    public static final String MAPPER_SCANNER_CONFIGURER_BANE_NAME = "mapperScannerConfigurer";
    public static final String ID_GENERATOR_BANE_NAME = "idGenerator";

    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
}
