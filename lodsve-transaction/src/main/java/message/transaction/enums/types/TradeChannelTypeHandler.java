package message.transaction.enums.types;

import message.mybatis.type.EnumCodeTypeHandler;
import message.transaction.enums.TradeChannel;
import org.apache.ibatis.type.MappedTypes;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/26 上午10:27
 */
@MappedTypes(TradeChannel.class)
public class TradeChannelTypeHandler extends EnumCodeTypeHandler<TradeChannel> {
    public TradeChannelTypeHandler() {
        super(TradeChannel.class);
    }
}
