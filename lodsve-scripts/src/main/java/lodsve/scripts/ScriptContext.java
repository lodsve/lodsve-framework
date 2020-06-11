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
