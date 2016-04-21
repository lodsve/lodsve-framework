package message.workflow.api;

import message.workflow.domain.FlowNode;
import message.workflow.domain.WorkTask;
import message.workflow.domain.Workflow;
import message.workflow.enums.AuditResult;

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
