package message.transaction.utils;

import message.transaction.enums.TradeType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注交易类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/21 下午1:42
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trade {
    /**
     * 功能描述:交易类型 <br>
     *
     * @return
     */
    TradeType value();
}
