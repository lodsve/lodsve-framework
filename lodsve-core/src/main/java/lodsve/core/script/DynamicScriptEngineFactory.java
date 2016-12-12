package lodsve.core.script;

import lodsve.core.script.groovy.GroovyScriptEngine;
import lodsve.core.script.js.JSScriptEngine;
import lodsve.core.script.python.PythonScriptEngine;
import lodsve.core.script.ruby.RubyScriptEngine;
import lodsve.core.script.spel.SpELScriptEngine;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据语言类型获取编译引擎的工厂.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午10:28
 */
public final class DynamicScriptEngineFactory {
    private static final Map<Script, ScriptEngine> SCRIPT_ENGINE = new HashMap<>();
    private static final String GROOVY_ENGINE_CLASS_NAME = "org.codehaus.groovy.jsr223.GroovyScriptEngineImpl";
    private static final String PYTHON_ENGINE_CLASS_NAME = "org.python.jsr223.PyScriptEngine";
    private static final String SPEL_ENGINE_CLASS_NAME = "org.springframework.expression.ExpressionParser";

    static {
        SCRIPT_ENGINE.put(Script.JS, new JSScriptEngine());
        if (ClassUtils.isPresent(GROOVY_ENGINE_CLASS_NAME, Thread.currentThread().getContextClassLoader())) {
            SCRIPT_ENGINE.put(Script.GROOVY, new GroovyScriptEngine());
        }
        if (ClassUtils.isPresent(PYTHON_ENGINE_CLASS_NAME, Thread.currentThread().getContextClassLoader())) {
            SCRIPT_ENGINE.put(Script.PYTHON, new PythonScriptEngine());
        }
        if (ClassUtils.isPresent(SPEL_ENGINE_CLASS_NAME, Thread.currentThread().getContextClassLoader())) {
            SCRIPT_ENGINE.put(Script.SPEL, new SpELScriptEngine());
        }
        SCRIPT_ENGINE.put(Script.RUBY, new RubyScriptEngine());
    }

    /**
     * 根据语言类型获取编译引擎
     *
     * @param script 语言类型
     * @return 编译引擎
     */
    public static ScriptEngine getEngine(Script script) {
        Assert.notNull(script, "类型不可为空!");

        return SCRIPT_ENGINE.get(script);
    }

    public enum Script {
        JS, GROOVY, PYTHON, SPEL, RUBY
    }
}
