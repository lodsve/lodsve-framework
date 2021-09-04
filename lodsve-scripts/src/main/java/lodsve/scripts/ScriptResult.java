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
