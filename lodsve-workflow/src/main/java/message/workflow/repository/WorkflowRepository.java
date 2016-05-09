package message.workflow.repository;

import java.util.List;
import message.workflow.domain.Workflow;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface WorkflowRepository {
    /**
     * 获取最新的一条流程记录，即version最大
     *
     * @param name name
     * @return
     */
    Workflow loadLatest(@Param("name") String name);

    /**
     * 根据domain查询流程
     *
     * @param domain
     * @return
     */
    List<Workflow> findByDomain(@Param("domain") String domain);

    /**
     * 保存流程
     *
     * @param workflow 流程
     */
    void save(Workflow workflow);
}
