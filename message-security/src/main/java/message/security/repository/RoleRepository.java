package message.security.repository;

import message.mybatis.repository.MyBatisRepository;
import message.security.pojo.Role;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/8 上午10:10
 */
@Repository
public interface RoleRepository extends MyBatisRepository<Role> {
    /**
     * 获取角色
     *
     * @param roleCode
     * @return
     */
    Role loadRole(@Param("roleCode") String roleCode);
}
