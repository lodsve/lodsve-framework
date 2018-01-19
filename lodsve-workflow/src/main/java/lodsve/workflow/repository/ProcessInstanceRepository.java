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

import lodsve.workflow.domain.ProcessInstance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 流程实例.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午4:20
 */
@Repository
public interface ProcessInstanceRepository {
    /**
     * 保存流程实例
     *
     * @param instance 流程实例
     */
    void save(ProcessInstance instance);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 流程实例
     */
    ProcessInstance findOne(@Param("id") Long id);

    /**
     * 更新流程实例
     *
     * @param instanceId       实例ID
     * @param currentNodeId    当前节点id
     * @param currentNodeTitle 当前节点标题
     */
    void updateProcessInstance(@Param("instanceId") Long instanceId, @Param("currentNodeId") Long currentNodeId,
                               @Param("currentNodeTitle") String currentNodeTitle);
}
