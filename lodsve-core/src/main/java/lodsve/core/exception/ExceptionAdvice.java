/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core.exception;

import lodsve.core.autoproperties.Env;
import lodsve.core.autoproperties.message.ResourceBundleHolder;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.core.utils.ResourceUtils;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 异常处理,返回前端类似于<code>{"code": 10001,"message": "test messages"}</code>的json数据.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/8/14 上午9:48
 */
@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    private static final ExceptionData DEFAULT_EXCEPTION_DATA = new ExceptionData(HttpStatus.BAD_REQUEST.value(), "发生未知的错误！");
    private static final Integer ASSERT_ERROR_CODE = 199999;

    /**
     * 加载了所有的资源文件信息.
     */
    private final ResourceBundleHolder resourceBundleHolder = new ResourceBundleHolder();

    @PostConstruct
    public void init() {
        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();

        // 1. 框架异常
        List<Resource> resources = new ArrayList<>();
        try {
            resources.addAll(Arrays.asList(resolver.getResources("classpath*:/META-INF/error/*.properties")));
        } catch (IOException e) {
            logger.error("resolver resource:'{classpath*:/META-INF/error/*.properties}' is error!", e);
            e.printStackTrace();
        }

        // 2. 项目异常
        String folderPath = "error";
        Resource resource = Env.getFileEnv(folderPath);

        try {
            String pathToUse = ResourceUtils.getPath(resource) + "/*.properties";

            resources.addAll(Arrays.asList(resolver.getResources(org.springframework.util.StringUtils.cleanPath(pathToUse))));
        } catch (IOException e) {
            logger.warn("resolver resource:'{" + resource + "}' is error!", e);
        }

        for (Resource r : resources) {
            this.resourceBundleHolder.loadMessageResource(r);
        }
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionData handleException(Exception ex, NativeWebRequest webRequest) {
        if (ex == null) {
            return DEFAULT_EXCEPTION_DATA;
        }

        if (ex instanceof ApplicationException) {
            // 框架定义的异常
            return handleWebException(ex, webRequest);
        } else if (ex instanceof IllegalArgumentException) {
            // spring assert exception
            return new ExceptionData(ASSERT_ERROR_CODE, ex.getMessage());
        }

        return new ExceptionData(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    private ExceptionData handleWebException(Exception ex, NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Throwable exception = getHasInfoException(ex);

        ExceptionInfo exceptionInfo = ((ApplicationException) exception).getInfo();
        String message;
        Integer code = exceptionInfo.getCode();

        try {
            message = resourceBundleHolder.getResourceBundle(request.getLocale()).getString(code.toString());
            message = PropertyPlaceholderHelper.replaceNumholder(message, message, exceptionInfo.getArgs());
        } catch (Exception e) {
            logger.error("根据异常编码获取异常描述信息发生异常，errorCode：" + code);
            message = exceptionInfo.getMessage();
        }

        if (StringUtils.isEmpty(message)) {
            message = "发生未知的错误！";
        }

        logger.error(String.format("exception code:[%s],exception message: %s", code, message), ex);

        return new ExceptionData(code, message);
    }

    private Throwable getHasInfoException(Throwable throwable) {
        Throwable exception = null;

        if (throwable instanceof ApplicationException) {
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

    private static class ExceptionData implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 异常编码
         */
        private Integer code;

        /**
         * 后台异常描述，正常不应该把后台异常描述反馈给前台用户
         */
        private String message;

        public ExceptionData() {
        }

        public ExceptionData(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
