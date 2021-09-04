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
package lodsve.core.utils;

import lodsve.core.bean.Constants;

import java.io.Serializable;

/**
 * 返回结果集.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-7-6 上午7:58
 */
public class ResultSet implements Serializable {
    public Boolean result = Boolean.FALSE;
    public Integer status;
    public String msg;

    /**
     * 构造器
     *
     * @param result 布尔型
     */
    public ResultSet(Boolean result) {
        this.result = result;

        this.status = result ? Constants.REQ_SUCCESS : Constants.REQ_FAILURE;
        this.msg = result ? Constants.REQ_SUCCESS_STR : Constants.REQ_FAILURE_STR;
    }

    /**
     * 构造器
     *
     * @param status 状态码
     */
    public ResultSet(Integer status) {
        this.status = status;

        this.msg = Constants.REQ_SUCCESS.equals(status) ? Constants.REQ_SUCCESS_STR : Constants.REQ_FAILURE_STR;
        this.result = Constants.REQ_SUCCESS.equals(status) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 构造器
     *
     * @param status 状态码
     * @param msg    信息
     */
    public ResultSet(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.result = Constants.REQ_SUCCESS.equals(status) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 构造器
     *
     * @param result 布尔值
     * @param msg    信息
     */
    public ResultSet(Boolean result, String msg) {
        this.result = result;
        this.status = result ? Constants.REQ_SUCCESS : Constants.REQ_FAILURE;
        this.msg = msg;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
