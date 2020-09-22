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
package lodsve.scripts;

import lodsve.core.utils.StringUtils;

/**
 * 执行结果.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/9 上午10:59
 */
public class ScriptResult {
    /**
     * 是否成功
     */
    private final boolean success;
    /**
     * 执行结果
     */
    private final Object result;
    /**
     * 执行信息
     */
    private final String message;
    /**
     * 发生错误抛出的异常
     */
    private transient final Throwable exception;
    /**
     * 执行时间
     */
    private final long useTime;

    private ScriptResult(boolean success, Object result, String message, Throwable exception, long useTime) {
        this.success = success;
        this.result = result;
        this.message = message;
        this.exception = exception;
        this.useTime = useTime;
    }

    public static ScriptResult success(Object result, long useTime) {
        return new ScriptResult(true, result, "success", null, useTime);
    }

    public static ScriptResult failure(String message, Throwable exception, long useTime) {
        return new ScriptResult(false, null, (StringUtils.isBlank(message) && exception != null) ? exception.getMessage() : message, exception, useTime);
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public long getUseTime() {
        return useTime;
    }

    @Override
    public String toString() {
        return result != null ? result.toString() : StringUtils.EMPTY;
    }
}
