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

import org.springframework.core.NestedRuntimeException;

/**
 * 系统中其他所有的异常均需要继承.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/8/14 下午12:27
 */
public class ApplicationException extends NestedRuntimeException {
    /**
     * 异常code
     */
    private Integer code;
    /**
     * 异常信息里的占位符参数
     */
    private String[] args;
    /**
     * 后台异常
     */
    private String content;

    public ApplicationException(String content) {
        super(content);
        this.content = content;
    }

    /**
     * @param code    异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content 后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     */
    public ApplicationException(Integer code, String content) {
        super(content);
        this.code = code;
        this.content = content;
    }

    /**
     * @param code    异常编码，在i18n配置文件中配置的编码，请确保该异常编码已经定义
     * @param content 后台异常内容，这个内容主要用于输出后台日志，便于异常诊断
     * @param args    在i18n配置文件中配置的错误描述中的占位符填充信息
     */
    public ApplicationException(Integer code, String content, String... args) {
        super(content);
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

    public ExceptionInfo getInfo() {
        return new ExceptionInfo(this.code, this.args, this.content);
    }
}
