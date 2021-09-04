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
package lodsve.mybatis.query;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * sql query.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public abstract class SqlQuery implements AutoCloseable {
    final Connection connection;
    private final DataSource dataSource;

    public SqlQuery(DataSource dataSource) {
        this.connection = DataSourceUtils.getConnection(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void close() throws Exception {
        closeResources();

        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    /**
     * 关闭其他资源
     *
     * @throws SQLException SQLException
     */
    public abstract void closeResources() throws SQLException;
}
