package lodsve.core.script;

import org.springframework.expression.Expression;

import javax.script.CompiledScript;

/**
 * 编译后自定义的上下文.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午10:52
 */
public class ScriptContext {
    private String id;
    private String md5;
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
