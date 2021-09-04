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
package lodsve.mybatis.commons;

import lodsve.mybatis.repository.bean.EntityTable;
import lodsve.mybatis.repository.helper.EntityHelper;
import lodsve.mybatis.repository.provider.BaseMapperProvider;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class TestBaseDaoMapperProvider extends BaseMapperProvider {
    public TestBaseDaoMapperProvider(Class<?> mapperClass) {
        super(mapperClass);
    }

    public String listAll(MappedStatement ms) {
        EntityTable table = EntityHelper.getEntityTable(getSelectReturnType(ms));

        return "SELECT * FROM " + table.getName();
    }
}
