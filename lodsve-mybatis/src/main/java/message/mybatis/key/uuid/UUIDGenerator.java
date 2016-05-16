package message.mybatis.key.uuid;

import java.util.UUID;
import message.mybatis.key.IDGenerator;

/**
 * uuid主键生成.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/9 下午1:10
 */
public class UUIDGenerator implements IDGenerator<String> {
    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
