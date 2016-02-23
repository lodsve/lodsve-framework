package message.wechat.beans.message.handler;

import message.wechat.beans.message.MsgType;
import message.wechat.beans.message.receive.Receive;
import message.wechat.beans.message.reply.Reply;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:25
 */
public interface MsgHandler<T extends Receive> {
    boolean support(MsgType msgType);

    Reply handle(T msg);
}
