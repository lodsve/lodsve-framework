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

package lodsve.core.script;

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
