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

import javax.sql.DataSource;

/**
 * 数据库方言.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:40
 */
public interface Dialect {
    /**
     * 获得查询所有条数的sql
     *
     * @param sql 原sql
     * @return 查询所有条数的sql
     */
    String getCountSql(String sql);

    /**
     * 获得分页的sql
     * 由于各个数据库分页语句不同，故让子类自己实现此方法
     *
     * @param sql    原sql
     * @param offset 偏移量
     * @param limit  数量
     * @return 分页的sql
     */
    String getPageSql(String sql, int offset, int limit);

    /**
     * 判断数据库中是否含有给定的表
     *
     * @param tableName  表名
     * @param dataSource 数据源
     * @return true/false
     * @throws Exception 获取数据源失败
     */
    boolean existTable(String tableName, DataSource dataSource) throws Exception;
}
