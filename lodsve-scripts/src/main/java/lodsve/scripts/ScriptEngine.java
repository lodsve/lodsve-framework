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

import javax.script.ScriptException;
import java.util.Map;

/**
 * 脚本语言编译引擎.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/9 上午10:28
 */
public interface ScriptEngine {
    /**
     * 引擎初始化
     *
     * @param args 参数
     * @throws ScriptException 脚本语言编译异常
     */
    void init(Object... args) throws ScriptException;

    /**
     * 编译脚本
     *
     * @param id         上下文中唯一标识
     * @param scriptText 脚本内容
     * @return 编译
     * @throws ScriptException 脚本语言编译异常
     */
    boolean compile(String id, String scriptText) throws ScriptException;

    /**
     * 判断是否对某一个id进行编译
     *
     * @param id id
     * @return 判断是否编译
     */
    boolean isCompiled(String id);

    /**
     * 移除编译后的缓存
     *
     * @param id 上下文中唯一标识
     * @return 移除是否成功
     */
    boolean remove(String id);

    /**
     * 执行
     *
     * @param id   上下文中唯一标识
     * @param args 参数
     * @return 执行结果
     */
    ScriptResult execute(String id, Map<String, Object> args);

    /**
     * 执行
     *
     * @param id 上下文中唯一标识
     * @return 执行结果
     */
    ScriptResult execute(String id);

    /**
     * 执行编译后的方法（指定方法名）
     *
     * @param id     上下文中唯一标识
     * @param method 方法名
     * @param args   参数
     * @return 执行结果
     * @throws ScriptException 脚本语言编译异常
     */
    ScriptResult invoke(String id, String method, Object... args) throws ScriptException;
}
