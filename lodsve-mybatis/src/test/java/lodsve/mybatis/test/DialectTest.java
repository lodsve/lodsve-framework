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

import com.google.common.collect.Lists;
import lodsve.mybatis.dialect.Dialect;
import lodsve.mybatis.dialect.MySqlDialect;
import org.junit.Test;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class DialectTest {
    @Test
    public void testGetCountSql() {
        Dialect dialect = new MySqlDialect();

        String sql = "select id, name from t_demo where id in (1,2,3,4,5) and sex = 1 order by id desc";
        System.out.println(dialect.getCountSql(sql));

        String sql2 = "select id, name from t_demo t join t_demo2 t2 on t.id = t2.demo_id where t1.id in (1,2,3,4,5) " +
                "and t1.sex = 1 and t2.id in (1,2,3,4) order by t1.id desc";
        System.out.println(dialect.getCountSql(sql2));

        String sql3 = "SELECT\n" +
                "        art_article.id as articleId,\n" +
                "        art_article.product_id as productId,\n" +
                "        art_article_code.type,\n" +
                "        art_article_code.code,\n" +
                "        art_article_inventory.depot_entry_time as depotEntryTime,\n" +
                "        art_article_inventory.depot_id as  depotId,\n" +
                "        art_article_inventory.status,\n" +
                "        art_article_box_detail.code as boxCode\n" +
                "        from art_article_inventory\n" +
                "        LEFT JOIN art_article_code on art_article_inventory.article_id = art_article_code.article_id\n" +
                "        LEFT JOIN art_article on art_article.id = art_article_inventory.article_id and art_article.enabled = 1\n" +
                "        LEFT JOIN art_article_box_detail on art_article_inventory.article_id = art_article_box_detail.article_id\n" +
                "        where art_article_inventory.enabled = 1\n" +
                " and art_article_inventory.depot_entry_time >= '2018-11-20'\n" +
                " and art_article_inventory.depot_entry_time < '2018-11-27'\n" +
                "GROUP BY art_article_inventory.article_id\n" +
                "ORDER BY NULL";
        System.out.println(dialect.getCountSql(sql3));
    }

    @Test
    public void testLambda() {
        List<String> list1 = Lists.newArrayList("Tom", "Jim", "Jack");
        List<String> list2 = Lists.newArrayList("Tom", "Jack");

        // true
        System.out.println(list1.stream().anyMatch(l -> l.contains("Jim")));
        // false
        System.out.println(list2.stream().anyMatch(l -> l.contains("Jim")));

        // false
        System.out.println(list1.stream().noneMatch(l -> l.contains("Jim")));
        // true
        System.out.println(list2.stream().noneMatch(l -> l.contains("Jim")));
    }
}
