package message.workflow.repository;

import message.mybatis.repository.MyBatisRepository;
import message.workflow.domain.Workflow;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface WorkflowRepository extends MyBatisRepository<Workflow> {
    /**
     * 获取最新的一条流程记录，即version最大
     *
     * @param code code
     * @return
     */
    Workflow loadLatest(@Param("code") String code);

    /**
     * 根据domain查询流程
     *
     * @param domain
     * @return
     */
    List<Workflow> findByDomain(@Param("domain") String domain);
}
