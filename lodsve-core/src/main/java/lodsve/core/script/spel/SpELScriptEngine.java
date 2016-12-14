package lodsve.core.script.spel;

import lodsve.core.script.ScriptContext;
import lodsve.core.script.ScriptEngine;
import lodsve.core.script.ScriptResult;
import lodsve.core.utils.EncryptUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.script.ScriptException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * spel script engine.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/10 上午12:01
 */
public class SpELScriptEngine implements ScriptEngine {
    /**
     * 编译后上下文的缓存
     */
    private static final Map<String, ScriptContext> SCRIPT_CONTENT_CACHE = new HashMap<>();
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public void init(Object... args) throws ScriptException {

    }

    @Override
    public boolean compile(String id, String scriptText) throws ScriptException {
        ScriptContext context = SCRIPT_CONTENT_CACHE.get(id);
        if (context != null && EncryptUtils.encodeMD5(scriptText).equals(context.getMd5())) {
            return context.getExpression() != null;
        }

        Expression expression = parser.parseExpression(scriptText);
        SCRIPT_CONTENT_CACHE.put(id, new ScriptContext(id, EncryptUtils.encodeMD5(scriptText), expression));

        return expression != null;
    }

    @Override
    public boolean isCompiled(String id) {
        return SCRIPT_CONTENT_CACHE.containsKey(id);
    }

    @Override
    public boolean remove(String id) {
        return SCRIPT_CONTENT_CACHE.remove(id) != null;
    }

    @Override
    public ScriptResult execute(String id, Map<String, Object> args) {
        long startTime = System.currentTimeMillis();
        ScriptContext scriptContext = SCRIPT_CONTENT_CACHE.get(id);
        if (scriptContext == null || scriptContext.getExpression() == null) {
            return ScriptResult.failure(String.format("script(spel): %s not found!", id), null, System.currentTimeMillis() - startTime);
        }

        EvaluationContext context = new StandardEvaluationContext(args);
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        Object obj = scriptContext.getExpression().getValue(context);

        return ScriptResult.success(obj, System.currentTimeMillis() - startTime);
    }

    @Override
    public ScriptResult execute(String id) {
        return execute(id, Collections.<String, Object>emptyMap());
    }

    @Override
    public ScriptResult invoke(String id, String method, Object... args) throws ScriptException {
        return ScriptResult.failure("spel script engine unsupport invoke!", null, 0);
    }
}
