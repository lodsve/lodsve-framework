package message.workflow.service;

import message.workflow.domain.FlowNode;
import message.workflow.domain.WorkTask;
import message.workflow.domain.Workflow;
import message.workflow.repository.FlowNodeRepository;
import message.workflow.repository.WorkTaskRepository;
import message.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * service.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 17:06
 */
@Service
public class WorkflowService {
    @Autowired
    private WorkflowRepository workflowRepository;
    @Autowired
    private FlowNodeRepository flowNodeRepository;
    @Autowired
    private WorkTaskRepository workTaskRepository;

    /**
     * 获取最新的工作流（版本号最大）
     *
     * @param code code
     * @return
     */
    public Workflow loadLatestWorkflow(String code) {
        Assert.hasText(code, "code不能为空！");

        return workflowRepository.loadLatest(code);
    }

    /**
     * 保存流程
     *
     * @param workflow
     * @return
     */
    public Workflow saveWorkflow(Workflow workflow) {
        Assert.notNull(workflow, "流程不能为空！");

        return workflowRepository.save(workflow);
    }

    /**
     * 保存流程节点
     *
     * @param flowNodes
     * @return
     */
    public List<FlowNode> saveFlowNodes(List<FlowNode> flowNodes) {
        Assert.notNull(flowNodes, "流程节点不能为空！");

        return null;//flowNodeRepository.save(flowNodes);
    }

    /**
     * 保存节点
     *
     * @param task
     */
    public void saveWorkTask(WorkTask task) {
        Assert.notNull(task);

        workTaskRepository.save(task);
    }

    /**
     * 通过流程id获取流程节点
     *
     * @param flowId 流程id
     * @return
     */
    public List<FlowNode> loadFlowNodeByFlowId(Long flowId) {
        Assert.notNull(flowId, "流程ID不能为空！");

        return flowNodeRepository.findByFlowId(flowId);
    }

    /**
     * 通过流程id获取流程节点
     *
     * @param flowIds 流程id
     * @return
     */
    public List<FlowNode> loadFlowNodeByFlowIds(List<Long> flowIds) {
        Assert.notNull(flowIds, "流程ID不能为空！");

        return flowNodeRepository.loadByFlowIds(flowIds);
    }

    /**
     * 根据domain查询流程
     *
     * @param domain
     * @return
     */
    public List<Workflow> findWorkflowByDomain(String domain) {
        Assert.hasText(domain);

        return workflowRepository.findByDomain(domain);
    }

    /**
     * 获取待办
     *
     * @param flowId     流程id
     * @param resourceId 资源id
     * @param domain     类名
     * @return
     */
    public WorkTask findUndoTask(Long flowId, Long resourceId, String domain) {
        Assert.notNull(flowId);
        Assert.notNull(resourceId);
        Assert.hasText(domain);

        return workTaskRepository.findUndoTask(flowId, resourceId, domain);
    }
}
