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
package lodsve.mybatis.test;

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
