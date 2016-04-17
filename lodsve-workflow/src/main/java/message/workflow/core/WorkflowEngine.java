package message.workflow.core;

import java.util.Date;
import java.util.List;
import message.base.utils.DateUtils;
import message.workflow.domain.FlowNode;
import message.workflow.domain.ProcessInstance;
import message.workflow.domain.WorkTask;
import message.workflow.domain.Workflow;
import message.workflow.enums.AuditResult;
import message.workflow.repository.ProcessInstanceRepository;
import message.workflow.repository.WorkTaskRepository;
import message.workflow.repository.WorkflowLocalStorage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 工作流对外核心接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-19 10:35
 */
@Component
public class WorkFlowEngine {
    @Autowired
    private WorkTaskRepository workTaskRepository;
    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    /**
     * 发起工作流
     *
     * @param resourceId   资源ID
     * @param domain       发起的目标对象class
     * @param launchUserId 发起人
     * @param launchUser   发起人姓名
     * @return 流程实例ID
     */
    public Long startProcess(Long resourceId, Class<?> domain, Long launchUserId, String launchUser) {
        Assert.notNull(domain);
        Assert.notNull(launchUserId);
        Assert.hasText(launchUser);

        Workflow workflow = findWorkflow(domain);
        // 开始节点
        FlowNode startNode = workflow.getStartNode();
        Assert.notNull(startNode, "开始节点不能为空！");

        Date time = DateUtils.getNow();
        WorkTask task = new WorkTask();
        task.setFlowId(workflow.getId());
        task.setNodeId(startNode.getId());
        task.setResourceId(resourceId);
        task.setProcessName(workflow.getTitle());
        task.setTaskName(startNode.getTitle());
        task.setUrlType(startNode.getUrlType());
        task.setFormUrl(startNode.getFormUrl().getUrl());
        task.setTaskUserId(launchUserId);
        task.setTaskUserName(launchUser);
        task.setReceiveTime(time);

        workTaskRepository.save(task);

        // 生成流程实例
        ProcessInstance instance = new ProcessInstance();
        instance.setFlowId(workflow.getId());
        instance.setFlowTitle(workflow.getTitle());
        instance.setUserId(launchUserId);
        instance.setUserName(launchUser);
        instance.setCurrentNodeId(startNode.getId());
        instance.setCurrentNodeTitle(startNode.getTitle());
        instance.setStartTime(time);

        processInstanceRepository.save(instance);

        return instance.getId();
    }

    /**
     * 流程办理
     *
     * @param target       发起的目标对象
     * @param launchUserId 办理人ID
     * @param launchUser   办理人姓名
     * @param result       办理结果
     * @param remark       办理意见
     */
    public void handler(Object target, Long launchUserId, String launchUser, AuditResult result, String remark) {
        Assert.notNull(target);
        Assert.notNull(launchUserId);
        Assert.hasText(launchUser);
        Assert.notNull(result);
        Assert.hasText(remark);

        Class<?> domain = target.getClass();
        Workflow workflow = findWorkflow(domain);

        // 获取当前的未办理的待办
        WorkTask task = workTaskRepository.findUndoTask(workflow.getId(), null, domain.getName());
    }

    private Workflow findWorkflow(Class<?> clazz) {
        List<Workflow> workflows = WorkflowLocalStorage.getWorkflow(clazz);
        if (CollectionUtils.isEmpty(workflows)) {
            throw new RuntimeException(String.format("%s对应的流程不存在，请检查！", clazz.getSimpleName()));
        }

        // 目前仅支持单一流程，即一个domain对应一个流程
        return workflows.get(0);
    }
}
