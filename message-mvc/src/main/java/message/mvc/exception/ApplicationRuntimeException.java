package message.mvc.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 系统中其他所有的异常均需要继承.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/14 下午12:27
 */
public class ApplicationRuntimeException extends NestedRuntimeException implements ExceptionInfoGetter {
    private Integer code;

    private String[] args;

    private String content;

    public ApplicationRuntimeException(String content) {
        super(content);
        this.content = content;
    }

    public ApplicationRuntimeException(String content, Throwable throwable) {
        super(content, throwable);
        this.content = content;
    }

    /**
     * @param code    异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content 后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     */
    public ApplicationRuntimeException(Integer code, String content) {
        super(content);
        this.code = code;
        this.content = content;
    }

    /**
     * @param code    异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content 后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     * @param args    在i18n配置文件中配置的错误描述中的占位符填充信息
     */
    public ApplicationRuntimeException(Integer code, String content, String... args) {
        super(content);
        this.code = code;
        this.args = args;
        this.content = content;
    }

    /**
     * @param code      异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content   后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     * @param throwable
     */
    public ApplicationRuntimeException(Integer code, String content, Throwable throwable) {
        super(content, throwable);
        this.code = code;
        this.content = content;
    }

    /**
     * @param code      异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content   后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     * @param args      在i18n配置文件中配置的错误描述中的占位符填充信息
     * @param throwable
     */
    public ApplicationRuntimeException(Integer code, String content, Throwable throwable, String... args) {
        super(content, throwable);
        this.code = code;
        this.args = args;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public ExceptionInfo getInfo() {
        ExceptionInfo exceptionInfo = new ExceptionInfo(this.code, this.args, this.content);
        return exceptionInfo;
    }
}
