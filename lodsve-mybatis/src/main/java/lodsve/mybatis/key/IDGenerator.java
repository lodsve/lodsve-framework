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

package lodsve.mybatis.key;

/**
 * make database id
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:10:32
 */
public interface IDGenerator {
    /**
     * get next long value
     *
     * @param sequenceName sequence name
     * @return next value
     */
    Long nextId(String sequenceName);

    enum KeyType {
        /**
         * twitter的snowflake的ID生成器实现
         */
        SNOWFLAKE,
        /**
         * MYSQL的方式
         */
        MYSQL,
        /**
         * ORACLE的方式
         */
        ORACLE
    }
}
