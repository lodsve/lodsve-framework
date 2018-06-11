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

import lodsve.workflow.domain.FlowNode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-11-18 14:19
 */
@Repository
public interface FlowNodeRepository {
    /**
     * 通过流程id获取流程节点
     *
     * @param flowId 流程id
     * @return
     */
    List<FlowNode> findByFlowId(@Param("flowId") Long flowId);

    /**
     * 通过流程id获取流程节点
     *
     * @param flowIds 流程id
     * @return
     */
    List<FlowNode> loadByFlowIds(@Param("flowIds") List<Long> flowIds);

    /**
     * 批量保存流程节点
     *
     * @param flowNodes 流程节点
     */
    void saveFlowNodes(List<FlowNode> flowNodes);

    /**
     * 获得流程当前节点
     *
     * @param processInstanceId 流程实例ID
     * @return 当前节点
     */
    FlowNode findCurrentNode(@Param("processInstanceId") Long processInstanceId);
}
