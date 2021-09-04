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

import java.io.Serializable;

/**
 * 异常信息定义.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/8/14 上午9:50
 */
public class ExceptionInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 异常编码
     */
    private Integer code;

    /**
     * 异常描述占位符替代内容列表
     */
    private String[] args;

    /**
     * 后台异常描述，正常不应该把后台异常描述反馈给前台用户
     */
    private String message;

    /**
     * 异常堆栈信息
     */
    private String detail;

    public ExceptionInfo() {

    }

    public ExceptionInfo(Integer code, String[] args) {
        this.code = code;
        this.args = args;
    }


    public ExceptionInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ExceptionInfo(Integer code, String[] args, String message) {
        this.code = code;
        this.args = args;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 异常信息配置文件中异常描述中占位符的替代数据
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * 异常信息配置文件中异常描述中占位符的替代数据
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
