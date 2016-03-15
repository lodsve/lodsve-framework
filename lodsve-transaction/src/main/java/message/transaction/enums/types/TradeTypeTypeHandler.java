package message.transaction.enums.types;

import message.mybatis.type.EnumCodeTypeHandler;
import message.transaction.enums.TradeType;
import org.apache.ibatis.type.MappedTypes;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/26 上午10:27
 */
@MappedTypes(TradeType.class)
public class TradeTypeTypeHandler extends EnumCodeTypeHandler<TradeType> {
    public TradeTypeTypeHandler() {
        super(TradeType.class);
    }
}
