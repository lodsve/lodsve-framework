package lodsve.workflow.repository;

import java.util.List;
import lodsve.workflow.domain.FlowNode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
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
