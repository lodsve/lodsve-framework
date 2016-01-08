package message.security.repository;

import message.mybatis.common.dao.BaseRepository;
import message.security.pojo.Account;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/8 上午10:10
 */
@Repository
public interface AccountRepository extends BaseRepository<Account> {
    /**
     * 根据登录名查找账户
     *
     * @param loginName
     * @return
     */
    Account loadAccount(@Param("loginName") String loginName);

    /**
     * 修改密码
     *
     * @param loginName
     * @param password
     */
    void chgPwd(@Param("loginName") String loginName, @Param("password") String password);
}
