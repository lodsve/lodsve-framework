package lodsve.validate.exception;

import java.util.List;

/**
 * 异常处理类,如果要实现自定义异常,则全部实现这个接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:08
 */
public interface ExceptionHandler {

    /**
     * 处理异常的方法
     *
     * @param messages 检验结果
     * @throws Exception
     */
    void doHandleException(List<ErrorMessage> messages) throws Exception;
}
