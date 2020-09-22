/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
