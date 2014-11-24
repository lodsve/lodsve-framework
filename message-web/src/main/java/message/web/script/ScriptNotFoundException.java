package message.web.script;

import message.base.exception.ApplicationRuntimeException;

/**
 * 脚本文件没有找到的异常.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-18 下午09:57:34
 */
public class ScriptNotFoundException extends ApplicationRuntimeException {
    public ScriptNotFoundException(int errorCode) {
        super(errorCode);
    }

    public ScriptNotFoundException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public ScriptNotFoundException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public ScriptNotFoundException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}
