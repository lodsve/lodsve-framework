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

package lodsve.workflow.api;

import lodsve.workflow.domain.FlowNode;
import lodsve.workflow.domain.WorkTask;
import lodsve.workflow.repository.FlowNodeRepository;
import lodsve.workflow.repository.WorkTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/19 下午3:05
 */
@Component
public class WorkflowOpenApi {
    @Autowired
    private WorkTaskRepository workTaskRepository;
    @Autowired
    private FlowNodeRepository flowNodeRepository;

    /**
     * 查询个人待办
     *
     * @param userId       办理人ID(必填)
     * @param flowId       流程id(非必填, 查询用)
     * @param processTitle 流程实例标题(非必填, 查询用)
     * @param pageable     分页信息
     * @return 待办的分页对象
     */
    public Page<WorkTask> listUndoTask(Long userId, Long flowId, String processTitle, Pageable pageable) {
        Assert.notNull(userId);
        Assert.notNull(flowId);
        Assert.hasText(processTitle);
        Assert.notNull(pageable);

        return workTaskRepository.listUndoTask(userId, flowId, processTitle, pageable);
    }

    /**
     * 获取流程实例当前节点
     *
     * @param processInstanceId 流程实例ID
     * @return 当前节点
     */
    public FlowNode findCurrentNode(Long processInstanceId) {
        Assert.notNull(processInstanceId);

        return flowNodeRepository.findCurrentNode(processInstanceId);
    }
}
