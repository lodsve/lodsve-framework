/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.mybatis.dialect;

/**
 * mysql数据库方言.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:42
 */
public class MySqlDialect extends AbstractDialect {
    @Override
    public String getPageSql(String sql, int offset, int limit) {
        StringBuilder sqlBuilder = new StringBuilder(sql);

        if (offset <= 0) {
            return sqlBuilder.append(" limit ").append(limit).toString();
        }
        return sqlBuilder.append(" limit ").append(offset).append(",").append(limit).toString();
    }

    @Override
    String existTableSql(String schema, String tableName) {
        String sql = "SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='%s' and TABLE_NAME = '%s'";
        return String.format(sql, schema, tableName);
    }
}
