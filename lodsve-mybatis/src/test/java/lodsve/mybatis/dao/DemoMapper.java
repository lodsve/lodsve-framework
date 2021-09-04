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
package lodsve.mybatis.dao;

import lodsve.mybatis.commons.TestBaseDAO;
import lodsve.mybatis.domain.Demo;
import lodsve.mybatis.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/9/27 5:17 PM
 */
public interface DemoMapper extends BaseRepository<Demo>, TestBaseDAO<Demo> {
    Page<Demo> list(Pageable pageable, @Param("name") String name, @Param("id") Integer id);

    boolean logicDelete2(@Param("id") Long id, @Param("value") Integer value);

    boolean updateNameById(@Param("id") Long id, @Param("name") String name);

    boolean updateAllColumnByEntity(Demo demo);
}
