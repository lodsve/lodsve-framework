package message.transaction.repository;

import message.mybatis.common.dao.BaseRepository;
import message.transaction.domain.Payment;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午7:42
 */
@Repository
public interface PaymentRepository extends BaseRepository<Payment> {
    Payment findByUnionId(String unionId);

    Payment findByTargetId(String targetId);

    Payment findByChargeId(String chargeId);
}
