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

package lodsve.scripts;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据语言类型获取编译引擎的工厂.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
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

    private DynamicScriptEngineFactory() {
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
}
