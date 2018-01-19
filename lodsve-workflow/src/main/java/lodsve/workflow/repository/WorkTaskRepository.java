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
import lodsve.workflow.domain.WorkTask;
import lodsve.workflow.enums.AuditResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 待办.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface WorkTaskRepository {
    /**
     * 保存待办
     *
     * @param workTask 待办
     */
    void save(WorkTask workTask);

    /**
     * 获取待办
     *
     * @param flowId     流程id
     * @param resourceId 资源id
     * @param domain     类名
     * @return
     */
    WorkTask findUndoTask(@Param("flowId") Long flowId, @Param("resourceId") Long resourceId, @Param("domain") String domain);

    /**
     * 批量保存
     *
     * @param workTasks task
     */
    void batchSave(List<WorkTask> workTasks);

    /**
     * 查询个人待办
     *
     * @param userId       办理人ID(必填)
     * @param flowId       流程id(非必填, 查询用)
     * @param processTitle 流程实例标题(非必填, 查询用)
     * @param pageable     分页信息
     * @return 待办的分页对象
     */
    Page<WorkTask> listUndoTask(@Param("userId") Long userId, @Param("flowId") Long flowId, @Param("processTitle") String processTitle, Pageable pageable);

    /**
     * 办理待办
     *
     * @param taskId 待办ID
     * @param result 结果
     * @param remark 备注
     */
    void doTask(@Param("taskId") Long taskId, @Param("result") AuditResult result, @Param("remark") String remark);
}
