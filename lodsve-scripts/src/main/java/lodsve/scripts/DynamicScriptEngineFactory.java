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
