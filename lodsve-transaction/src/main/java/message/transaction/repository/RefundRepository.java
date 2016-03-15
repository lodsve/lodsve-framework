package message.transaction.repository;

import message.mybatis.repository.MyBatisRepository;
import message.transaction.domain.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午7:43
 */
@Repository
public interface RefundRepository extends MyBatisRepository<Refund> {
    Refund findByPaymentId(@Param("paymentId") Long paymentId);

    Refund findByChargeId(@Param("chargeId") String chargeId);

    Page<Refund> findRefunds(Pageable pageable);
}
