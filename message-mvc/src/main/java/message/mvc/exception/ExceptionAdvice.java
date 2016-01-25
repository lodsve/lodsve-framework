package message.mvc.exception;

import message.config.MessageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理,返回前端类似于<code>{"code": 10001,"message": "test messages"}</code>的json数据.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/14 上午9:48
 */
@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionData handleException(Exception ex, NativeWebRequest webRequest) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        ExceptionData exceptionData;
        Throwable exception = getHasInfoException(ex);
        if (exception == null) {
            exceptionData = new ExceptionData(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        } else {
            ExceptionInfo exceptionInfo = ((ExceptionInfoGetter) exception).getInfo();
            String message;
            Integer code = exceptionInfo.getCode();
            if (code != null) {
                try {
                    message = MessageConfig.getMessage(code.toString(), request.getLocale(), exceptionInfo.getArgs());
                } catch (Exception e) {
                    logger.error("根据异常编码获取异常描述信息发生异常，errorCode：" + code);
                    message = exceptionInfo.getMessage();
                }
            } else {
                // 兼容以前的代码，初始版本只有message没有code
                message = exceptionInfo.getMessage();
            }

            if (StringUtils.isEmpty(message)) {
                message = "发生未知的错误";
            }
            exceptionData = new ExceptionData(code, message);
        }

        logger.error("exception code:" + exceptionData.getCode() + ",exception message:" + exceptionData.getMessage(),
                ex);

        return exceptionData;
    }

    private Throwable getHasInfoException(Throwable throwable) {
        Throwable exception = null;

        if (throwable instanceof ExceptionInfoGetter) {
            exception = throwable;
        }

        Throwable childThrowable;
        if (throwable instanceof UndeclaredThrowableException) {
            childThrowable = ((UndeclaredThrowableException) throwable).getUndeclaredThrowable();
        } else {
            childThrowable = throwable.getCause();
        }
        if (childThrowable != null) {
            Throwable childExp = getHasInfoException(childThrowable);
            if (childExp != null) {
                return childExp;
            }
        }

        return exception;
    }
}
