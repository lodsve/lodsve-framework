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

import org.springframework.expression.Expression;

import javax.script.CompiledScript;

/**
 * 编译后自定义的上下文.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/9 上午10:52
 */
public class ScriptContext {
    private final String id;
    private final String md5;
    private CompiledScript script;
    private Expression expression;
    private Object object;

    ScriptContext(String id, String md5, CompiledScript script) {
        this.id = id;
        this.md5 = md5;
        this.script = script;
    }

    public ScriptContext(String id, String md5, Expression expression) {
        this.id = id;
        this.md5 = md5;
        this.expression = expression;
    }

    public ScriptContext(String id, String md5, Object object) {
        this.id = id;
        this.md5 = md5;
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public String getMd5() {
        return md5;
    }

    public CompiledScript getScript() {
        return script;
    }

    public Expression getExpression() {
        return expression;
    }

    public Object getObject() {
        return object;
    }
}
