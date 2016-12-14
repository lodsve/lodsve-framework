package lodsve.core.script;

import lodsve.core.utils.StringUtils;

/**
 * 执行结果.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午10:59
 */
public class ScriptResult {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 执行结果
     */
    private Object result;
    /**
     * 执行信息
     */
    private String message;
    /**
     * 发生错误抛出的异常
     */
    private transient Throwable exception;
    /**
     * 执行时间
     */
    private long useTime;

    private ScriptResult(boolean success, Object result, String message, Throwable exception, long useTime) {
        this.success = success;
        this.result = result;
        this.message = message;
        this.exception = exception;
        this.useTime = useTime;
    }

    public static ScriptResult success(Object result, long useTime) {
        return new ScriptResult(true, result, "success", null, useTime);
    }

    public static ScriptResult failure(String message, Throwable exception, long useTime) {
        return new ScriptResult(false, null, (StringUtils.isBlank(message) && exception != null) ? exception.getMessage() : message, exception, useTime);
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public long getUseTime() {
        return useTime;
    }

    @Override
    public String toString() {
        return result != null ? result.toString() : StringUtils.EMPTY;
    }
}
