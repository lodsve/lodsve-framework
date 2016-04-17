package message.workflow.repository;

import message.mybatis.repository.MyBatisRepository;
import message.workflow.domain.ProcessInstance;
import org.springframework.stereotype.Repository;

/**
 * 流程实例.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午4:20
 */
@Repository
public interface ProcessInstanceRepository extends MyBatisRepository<ProcessInstance> {
}
