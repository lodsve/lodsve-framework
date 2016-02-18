package message.workflow.domain;

import message.workflow.enums.AuditResult;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工作流任务.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 下午1:43
 */
@Entity
@Table(name = "t_work_task")
public class WorkTask {
    @Id
    @GeneratedValue(generator = "snowflake")
    private Long id;
    /**
     * 流程ID
     */
    @Column
    private Long flowId;
    /**
     * 节点ID
     */
    @Column
    private Long nodeId;
    /**
     * 关联的资源ID
     */
    @Column
    private Long resourceId;
    /**
     * 流程名称
     */
    @Column
    private String processName;
    /**
     * 任务描述
     */
    @Column
    private String taskName;
    /**
     * 办理人Id
     */
    @Column
    private Long taskUserId;
    /**
     * 办理人姓名
     */
    @Column
    private String taskUserName;
    /**
     * 审核结果(为空即待办)
     */
    @Column
    private AuditResult result;
    /**
     * 处理意见
     */
    @Column
    private String remark;
    /**
     * 接收时间
     */
    @Column
    private Long receiveTime = System.currentTimeMillis();
    /**
     * 办理时间
     */
    @Column
    private Long handleTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public String getTaskUserName() {
        return taskUserName;
    }

    public void setTaskUserName(String taskUserName) {
        this.taskUserName = taskUserName;
    }

    public AuditResult getResult() {
        return result;
    }

    public void setResult(AuditResult result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }
}
