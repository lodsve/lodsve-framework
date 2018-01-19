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
import lodsve.workflow.domain.Workflow;
import lodsve.workflow.enums.AuditResult;

/**
 * 流程办理拦截器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-19 16:38
 */
public interface HandlerInterceptor {

    /**
     * 办理之前处理事情
     *
     * @param workflow 流程定义
     * @param node     流程节点定义
     * @param task     当前节点
     * @param result   办理结果
     * @param handler  办理人
     * @param remark   办理意见
     */
    void preNodeHandler(Workflow workflow, FlowNode node, WorkTask task, AuditResult result, Long handler, String remark);

    /**
     * 办理之后处理事情
     *
     * @param workflow 流程定义
     * @param node     流程节点定义
     * @param task     当前节点
     * @param result   办理结果
     * @param handler  办理人
     * @param remark   办理意见
     */
    void postNodeHandler(Workflow workflow, FlowNode node, WorkTask task, AuditResult result, Long handler, String remark);
}
