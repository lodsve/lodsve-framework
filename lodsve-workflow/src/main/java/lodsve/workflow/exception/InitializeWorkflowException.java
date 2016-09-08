package lodsve.workflow.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 流程初始化异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/22 下午1:08
 */
public class InitializeWorkflowException extends ApplicationException {
    public InitializeWorkflowException(String content) {
        super(content);
    }

    public InitializeWorkflowException(Integer code, String content) {
        super(code, content);
    }

    public InitializeWorkflowException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}
