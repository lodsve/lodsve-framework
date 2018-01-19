/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.utils;

import lodsve.core.bean.Constants;

import java.io.Serializable;

/**
 * 返回结果集.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-7-6 上午7:58
 */
public class ResultSet implements Serializable {
    public Boolean result = Boolean.FALSE;
    public Integer status;
    public String msg;

    /**
     * 构造器
     *
     * @param result        布尔型
     */
    public ResultSet(Boolean result) {
        this.result = result;

        this.status = result ? Constants.REQ_SUCCESS : Constants.REQ_FAILURE;
        this.msg = result ? Constants.REQ_SUCCESS_STR : Constants.REQ_FAILURE_STR;
    }

    /**
     * 构造器
     *
     * @param status        状态码
     */
    public ResultSet(Integer status) {
        this.status = status;

        this.msg = Constants.REQ_SUCCESS.equals(status) ? Constants.REQ_SUCCESS_STR : Constants.REQ_FAILURE_STR;
        this.result = Constants.REQ_SUCCESS.equals(status) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 构造器
     *
     * @param status        状态码
     * @param msg           信息
     */
    public ResultSet(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.result = Constants.REQ_SUCCESS.equals(status) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 构造器
     *
     * @param result        布尔值
     * @param msg           信息
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
