package message.utils;

import message.base.Constants;

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
