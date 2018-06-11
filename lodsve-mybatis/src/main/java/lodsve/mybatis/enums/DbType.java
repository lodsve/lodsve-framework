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

package lodsve.mybatis.enums;

import org.springframework.util.Assert;

/**
 * 表示数据库类型的枚举.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/6/1 下午4:18
 */
public enum DbType {
    /**
     * 数据库类型
     */
    DB_ORACLE("O", "Oracle"), DB_MYSQL("M", "MySQL");

    /**
     * DbType
     *
     * @param dbType String
     * @param name   String
     */
    DbType(String dbType, String name) {
        this.dbType = dbType;
        this.name = name;
    }

    /**
     * dbType
     */
    private String dbType;

    /**
     * name
     */
    private String name;

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Description: <br>
     *
     * @param eval String
     * @return <br>
     */
    public static DbType eval(String eval) {
        Assert.hasLength(eval, "eval is required!");

        for (DbType dt : DbType.values()) {
            if (eval.equals(dt.getDbType())) {
                return dt;
            }
        }

        return null;
    }
}
