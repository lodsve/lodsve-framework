package message.workflow.core;

import java.util.List;

/**
 * 解析办理人.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午3:57
 */
public interface ConditionalResolver {
    /**
     * 解析办理人
     *
     * @param conditional 表达式
     * @return 办理人
     */
    List<Long> resolveHandlers(String conditional);
}
