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

import lodsve.mybatis.query.NativeSqlQuery;
import lodsve.mybatis.utils.SqlUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * 公用部分.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:52
 */
public abstract class AbstractDialect implements Dialect {
    @Override
    public String getCountSql(String sql) {
        return SqlUtils.getSingleLineCountSql(sql);
    }

    @Override
    public boolean existTable(String tableName, DataSource dataSource) throws Exception {
        Assert.notNull(dataSource, "dataSource must be non-null!");
        Assert.hasText(tableName, "dataSource must be non-null!");

        try (NativeSqlQuery query = new NativeSqlQuery(dataSource)) {
            return 0 < query.queryForInt(existTableSql(tableName, tableName));
        }
    }

    /**
     * 判断表是否存在的sql
     *
     * @param schema    schema
     * @param tableName table Name
     * @return 判断表是否存在的sql
     */
    abstract String existTableSql(String schema, String tableName);
}
