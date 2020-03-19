/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
