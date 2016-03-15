package message.wechat.api.message;

import message.wechat.beans.message.MsgType;
import message.wechat.beans.message.receive.Receive;
import message.wechat.beans.message.reply.Reply;

/**
 * 对接受消息处理器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:25
 */
public interface MsgHandler<T extends Receive> {
    /**
     * 是否支持此消息
     *
     * @param msgType 接受得到的消息
     * @return true/false
     */
    boolean support(MsgType msgType);

    /**
     * 处理消息并返回
     *
     * @param msg 消息
     * @return 返回消息
     */
    Reply handle(T msg);

    /**
     * 获得消息类型
     *
     * @return 消息类型
     */
    Class<T> getType();
}
