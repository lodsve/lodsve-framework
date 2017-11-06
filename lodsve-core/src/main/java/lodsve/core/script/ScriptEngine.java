package lodsve.core.script;

import javax.script.ScriptException;
import java.util.Map;

/**
 * 脚本语言编译引擎.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午10:28
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
