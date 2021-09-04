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
package lodsve.web.mvc.debug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lodsve.web.mvc.properties.DebugConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 打印请求参数.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Aspect
public class DebugRequestAspect {
    private static final Logger logger = LoggerFactory.getLogger(DebugRequestAspect.class);
    private static final String COMMA = ",";
    private static final List<Pattern> PATTERNS = new ArrayList<>(16);
    private static final List<String> DEFAULT_EXCLUDE_URL = new ArrayList<>();

    private final ObjectMapper objectMapper;
    private final DebugConfig debugConfig;

    public DebugRequestAspect(ObjectMapper objectMapper, DebugConfig debugConfig) {
        this.objectMapper = objectMapper;

        DEFAULT_EXCLUDE_URL.add(".*/v2/api-docs");
        DEFAULT_EXCLUDE_URL.add(".*/swagger-resources");
        DEFAULT_EXCLUDE_URL.add(".*/configuration/ui");
        DEFAULT_EXCLUDE_URL.add(".*/webjars/.*");
        DEFAULT_EXCLUDE_URL.add(".*/swagger-ui.html");
        this.debugConfig = debugConfig;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object deBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        if (!checkUrl(request)) {
            return joinPoint.proceed();
        }

        before(joinPoint);

        StopWatch watch = new StopWatch();
        watch.start("");
        Object object = joinPoint.proceed();
        watch.stop();

        after(object, watch.getTotalTimeMillis());

        return object;
    }

    private boolean checkUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String client = request.getRemoteHost();

        if (PATTERNS.isEmpty()) {
            initPattern();
        }

        if (debugConfig.getExcludeAddress().contains(client)) {
            return false;
        }
        for (Pattern pattern : PATTERNS) {
            if (pattern.matcher(url).matches()) {
                return false;
            }
        }

        return true;
    }

    private void initPattern() {
        List<String> excludeUrl = debugConfig.getExcludeUrl();
        excludeUrl.addAll(DEFAULT_EXCLUDE_URL);
        for (String url : excludeUrl) {
            PATTERNS.add(Pattern.compile(url));
        }
    }

    private void before(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> names = request.getHeaderNames();

        List<String> header = Lists.newArrayList();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            header.add(name + " = " + request.getHeader(name));
        }

        if (logger.isInfoEnabled()) {
            String message = "\n请求相关信息：\n【请求头信息】->【{}】,\n【请求方法】->【{}】,\n【请求参数】->【{}】";
            Object[] args = joinPoint.getArgs();
            List<String> toString = Lists.newArrayList();
            for (Object arg : args) {
                toString.add(ToStringBuilder.reflectionToString(arg, ToStringStyle.JSON_STYLE));
            }

            logger.info(message, StringUtils.join(header, COMMA), joinPoint.getSignature(), toString);
        }
    }

    private void after(Object object, long totalTimeMillis) throws JsonProcessingException {
        if (logger.isInfoEnabled()) {
            String message = "\n执行情况：\n执行时间为：【{}毫秒】\n返回值为：【{}】";
            String objectMsg;
            if (object instanceof Serializable) {
                objectMsg = objectMapper.writeValueAsString(object);
            } else {
                objectMsg = object == null ? "no return" : ToStringBuilder.reflectionToString(object, ToStringStyle.JSON_STYLE);
            }

            logger.info(message, totalTimeMillis, objectMsg);
        }
    }
}
