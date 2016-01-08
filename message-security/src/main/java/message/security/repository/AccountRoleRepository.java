package message.security.repository;

import message.mybatis.common.dao.BaseRepository;
import message.security.pojo.AccountRole;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/8 上午10:10
 */
@Repository
public interface AccountRoleRepository extends BaseRepository<AccountRole> {
    /**
     * 删除角色关系
     *
     * @param account
     */
    void deleteAccountRoles(@Param("account") String account);

    /**
     * 获取角色
     *
     * @param loginName
     * @return
     */
    List<String> loadRoleByAccount(@Param("loginName") String loginName);
}
