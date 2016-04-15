package message.workflow.repository;

import message.mybatis.repository.MyBatisRepository;
import message.workflow.domain.WorkTask;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface WorkTaskRepository extends MyBatisRepository<WorkTask> {
    /**
     * 获取待办
     *
     * @param flowId     流程id
     * @param resourceId 资源id
     * @param domain     类名
     * @return
     */
    WorkTask findUndoTask(@Param("flowId") Long flowId, @Param("resourceId") Long resourceId, @Param("domain") String domain);
}
