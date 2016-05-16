package lodsve.transaction.repository;

import lodsve.transaction.domain.Payment;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午7:42
 */
@Repository
public interface PaymentRepository {
    void save(Payment payment);

    Payment findOne(@Param("id") Long id);

    Payment findByUnionId(@Param("unionId") String unionId);

    Payment findByTargetId(@Param("targetId") String targetId);

    Payment findByChargeId(@Param("chargeId") String chargeId);
}
