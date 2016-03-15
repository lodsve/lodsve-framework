package message.transaction.enums.types;

import message.mybatis.type.EnumCodeTypeHandler;
import message.transaction.enums.TradeResult;
import org.apache.ibatis.type.MappedTypes;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/26 上午10:27
 */
@MappedTypes(TradeResult.class)
public class TradeResultTypeHandler extends EnumCodeTypeHandler<TradeResult> {
    public TradeResultTypeHandler() {
        super(TradeResult.class);
    }
}
