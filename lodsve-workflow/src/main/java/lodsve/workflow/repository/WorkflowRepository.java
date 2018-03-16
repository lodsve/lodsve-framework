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

package lodsve.workflow.repository;

import java.util.List;
import lodsve.workflow.domain.Workflow;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface WorkflowRepository {
    /**
     * 获取最新的一条流程记录，即version最大
     *
     * @param name name
     * @return
     */
    Workflow loadLatest(@Param("name") String name);

    /**
     * 根据domain查询流程
     *
     * @param domain
     * @return
     */
    List<Workflow> findByDomain(@Param("domain") String domain);

    /**
     * 保存流程
     *
     * @param workflow 流程
     */
    void save(Workflow workflow);
}
