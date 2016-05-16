package message.workflow.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import message.base.utils.DateUtils;
import message.base.utils.ListUtils;
import message.mybatis.key.IDGenerator;
import message.mybatis.utils.MyBatisUtils;
import message.workflow.api.ConditionalResolver;
import message.workflow.api.HandlerInterceptor;
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
public class WorkflowEngine {
    @Autowired
    private WorkTaskRepository workTaskRepository;
    @Autowired
    private ProcessInstanceRepository processInstanceRepository;
    @Autowired
    private ConditionalResolver resolver;

    /**
     * 发起工作流
     *
     * @param resourceId   资源ID
     * @param domain       发起的目标对象class
     * @param launchUserId 发起人
     * @param launchUser   发起人姓名
     * @return 流程实例ID
     */
    public Long startProcess(Long resourceId, String title, Class<?> domain, Long launchUserId, String launchUser) {
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
        task.setFlowTitle(workflow.getTitle());
        task.setNodeTitle(startNode.getTitle());
        task.setUrlType(startNode.getUrlType());
        task.setTaskUserId(launchUserId);
        task.setTaskUserName(launchUser);
        task.setReceiveTime(time);
        task.setResult(AuditResult.UNDO);
        task.setProcessTitle(title);

        workTaskRepository.save(task);

        // 生成流程实例
        ProcessInstance instance = new ProcessInstance();
        instance.setFlowId(workflow.getId());
        instance.setFlowTitle(workflow.getTitle());
        instance.setStartUserId(launchUserId);
        instance.setStartUserName(launchUser);
        instance.setCurrentNodeId(startNode.getId());
        instance.setCurrentNodeTitle(startNode.getTitle());
        instance.setStartTime(time);
        instance.setResourceId(resourceId);
        instance.setProcessTitle(title);

        processInstanceRepository.save(instance);
        return instance.getId();
    }

    /**
     * 流程办理
     *
     * @param processInstanceId 流程实例ID
     * @param domain            发起的目标对象class
     * @param resourceId        资源ID
     * @param launchUserId      办理人ID
     * @param launchUser        办理人姓名
     * @param result            办理结果
     * @param remark            办理意见
     */
    public void handler(Long processInstanceId, Class<?> domain, Long resourceId, Long launchUserId, String launchUser, AuditResult result, String remark) {
        Assert.notNull(processInstanceId);
        Assert.notNull(domain);
        Assert.notNull(resourceId);
        Assert.notNull(launchUserId);
        Assert.notNull(launchUser);
        Assert.notNull(result);
        Assert.hasText(remark);

        // processInstance
        ProcessInstance instance = processInstanceRepository.findOne(processInstanceId);
        Assert.notNull(instance, String.format("流程示例不存在!流程示例id: %s!", processInstanceId));

        // 获得流程定义
        Workflow workflow = findWorkflow(domain);

        // 获取当前待办
        WorkTask task = workTaskRepository.findUndoTask(workflow.getId(), resourceId, domain.getName());
        Assert.notNull(task, String.format("当前待办不存在,流程名: %s, 流程实例ID: %s, 当前待办ID: %s", instance.getFlowTitle(), processInstanceId, task.getId()));

        // 获取当前节点
        List<FlowNode> nodes = workflow.getNodes();
        final Long currentNodeId = instance.getCurrentNodeId();
        FlowNode node = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
            @Override
            public boolean judge(FlowNode target) {
                return target.getId().equals(currentNodeId);
            }
        });
        Assert.notNull(node, String.format("当前节点不存在!流程名: %s, 流程实例ID: %s, 流程节点: %s, 待办ID: %s",
                workflow.getTitle(), processInstanceId, currentNodeId, task.getId()));

        // 获得下一节点
        final String next = node.getNext();
        FlowNode nextNode = ListUtils.findOne(nodes, new ListUtils.Decide<FlowNode>() {
            @Override
            public boolean judge(FlowNode target) {
                return target.getName().equals(next);
            }
        });
        Assert.notNull(nextNode, String.format("当前节点下一节点不存在!流程名: %s, 流程实例ID: %s, 当前节点: %s, 待办ID: %s, 下一节点name: %s",
                workflow.getTitle(), processInstanceId, currentNodeId, task.getId(), next));

        // 获得下一节点的handlerInterceptor
        HandlerInterceptor interceptor = node.getInterceptor();
        // 获得下一节点的办理人表达式
        String conditional = node.getConditional();
        List<Long> handlerUserIds = resolver.resolveHandlers(conditional);

        // 开始办理...

        // 办理前做的事情
        if (interceptor != null) {
            interceptor.preNodeHandler(workflow, node, task, result, launchUserId, remark);
        }
        // 办理
        // 1. 办掉当前事项
        workTaskRepository.doTask(task.getId(), result, remark);
        // 2. 生成下一步待办,如果是多个人办理,就生成多条待办
        List<WorkTask> tasks = new ArrayList<>(handlerUserIds.size());
        for (Long handlerUserId : handlerUserIds) {
            WorkTask nextTask = new WorkTask();

            Long id = MyBatisUtils.nextId(IDGenerator.KeyType.SNOWFLAKE);
            nextTask.setId(id);
            nextTask.setFlowId(workflow.getId());
            nextTask.setNodeId(nextNode.getId());
            nextTask.setResourceId(task.getResourceId());
            nextTask.setFlowTitle(workflow.getTitle());
            nextTask.setNodeTitle(nextNode.getTitle());
            nextTask.setProcessTitle(task.getProcessTitle());
            nextTask.setUrlType(nextNode.getUrlType());
            nextTask.setTaskUserId(handlerUserId);
            nextTask.setTaskUserName(resolver.resolveHandlerName(handlerUserId));
            nextTask.setReceiveTime(new Date());
            nextTask.setResult(AuditResult.UNDO);

            tasks.add(nextTask);
        }
        workTaskRepository.batchSave(tasks);
        // 3. 更新流程实例
        processInstanceRepository.updateProcessInstance(instance.getId(), nextNode.getId(), nextNode.getTitle());

        // 办理之后要做的事情
        if (interceptor != null) {
            interceptor.postNodeHandler(workflow, node, task, result, launchUserId, remark);
        }
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
