package message.workflow.repository;

import message.workflow.domain.ProcessInstance;
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
