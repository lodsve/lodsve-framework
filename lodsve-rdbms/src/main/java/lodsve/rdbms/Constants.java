/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.rdbms;

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

    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";

    public static final String FLYWAY_BEAN_NAME = "lodsveFlyway";
}
