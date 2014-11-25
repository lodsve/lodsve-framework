package message.utils.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 文件夹已存在的异常
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-16 下午08:30:51
 */
public class FileExistException extends ApplicationRuntimeException {

    public FileExistException(int errorCode) {
        super(errorCode);
    }

    public FileExistException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public FileExistException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public FileExistException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}
