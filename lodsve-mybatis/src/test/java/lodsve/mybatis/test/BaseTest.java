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
package lodsve.mybatis.test;

import lodsve.mybatis.utils.DbType;
import lodsve.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/9/27 5:57 PM
 */
public abstract class BaseTest {
    private static final String DB_TYPE_HSQLDB = "hsqldb";
    private static final String DB_TYPE = DB_TYPE_HSQLDB;
    static SqlSession session;

    @BeforeClass
    public static void before() {
        try (Reader reader = Resources.getResourceAsReader("sql/init-" + DB_TYPE + ".sql")) {
            String configFile = "mybatis-config.xml";
            InputStream is = Resources.getResourceAsStream(configFile);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is, DB_TYPE);
            session = factory.openSession();

            Connection conn = session.getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(null);
            runner.runScript(reader);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyBatisUtils.setDbType(DbType.DB_MYSQL);
    }

    @After
    public void commit() {
        session.commit();
    }

    @AfterClass
    public static void destroy() {
        session.commit();
        try (Reader reader = Resources.getResourceAsReader("sql/destroy.sql")) {
            Connection conn = session.getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(null);
            runner.runScript(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.commit();
        session.close();
        session = null;
    }
}
