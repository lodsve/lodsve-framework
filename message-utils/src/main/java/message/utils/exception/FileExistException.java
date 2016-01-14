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
    public FileExistException(String content) {
        super(content);
    }

    public FileExistException(Integer code, String content) {
        super(code, content);
    }

    public FileExistException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}
