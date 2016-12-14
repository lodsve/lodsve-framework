package lodsve.core.script;

import lodsve.core.utils.EncryptUtils;

import javax.script.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 其实就是使用jdk自带的脚本引擎.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午10:47
 */
public abstract class AbstractScriptEngine implements ScriptEngine {
    /**
     * 编译器
     */
    private Compilable compilable;
    /**
     * 未初始化的参数绑定
     */
    private Bindings uninitBindings;
    /**
     * 编译后上下文的缓存
     */
    private static final Map<String, ScriptContext> SCRIPT_CONTENT_CACHE = new HashMap<>();

    public AbstractScriptEngine() {
        try {
            init();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Object... args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        javax.script.ScriptEngine engine = manager.getEngineByName(getScriptName());
        compilable = (Compilable) engine;
        uninitBindings = engine.createBindings();
        CompiledScript script;
        for (Object arg : args) {
            script = compilable.compile(arg.toString());
            script.eval(uninitBindings);
        }
        SCRIPT_CONTENT_CACHE.clear();
    }

    @Override
    public boolean compile(String id, String scriptText) throws ScriptException {
        if (compilable == null) {
            init();
        }

        ScriptContext context = SCRIPT_CONTENT_CACHE.get(id);
        if (context != null && EncryptUtils.encodeMD5(scriptText).equals(context.getMd5())) {
            return context.getScript() != null;
        }

        CompiledScript compiledScript = compilable.compile(scriptText);
        SCRIPT_CONTENT_CACHE.put(id, new ScriptContext(id, EncryptUtils.encodeMD5(scriptText), compiledScript));

        return compiledScript != null;
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
        ScriptContext script = SCRIPT_CONTENT_CACHE.get(id);

        if (script == null || script.getScript() == null) {
            return ScriptResult.failure(String.format("script(%s): %s not found!", getScriptName(), id), null, System.currentTimeMillis() - startTime);
        }

        Object result = null;
        boolean success = false;
        String message = "";
        Exception exception = null;
        try {
            javax.script.ScriptContext context = new SimpleScriptContext();
            context.setBindings(uninitBindings, javax.script.ScriptContext.GLOBAL_SCOPE);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                context.setAttribute(entry.getKey(), entry.getValue(), javax.script.ScriptContext.ENGINE_SCOPE);
            }

            result = script.getScript().eval(context);
            success = true;
        } catch (ScriptException e) {
            exception = e;
        }

        long useTime = System.currentTimeMillis() - startTime;
        if (success) {
            return ScriptResult.success(result, useTime);
        } else {
            return ScriptResult.failure(message, exception, useTime);
        }
    }

    @Override
    public ScriptResult execute(String id) {
        return execute(id, Collections.<String, Object>emptyMap());
    }

    @Override
    public ScriptResult invoke(String id, String method, Object... args) throws ScriptException {
        long startTime = System.currentTimeMillis();
        ScriptContext script = SCRIPT_CONTENT_CACHE.get(id);

        if (script == null || script.getScript() == null) {
            return ScriptResult.failure(String.format("script(%s): %s not found!", getScriptName(), id), null, System.currentTimeMillis() - startTime);
        }

        Bindings bindings = script.getScript().getEngine().getBindings(javax.script.ScriptContext.ENGINE_SCOPE);
        script.getScript().eval(bindings);

        Invocable invocable = (Invocable) script.getScript().getEngine();

        boolean result = false;
        Object value = null;
        String message = "";
        Exception exception = null;
        try {
            value = invocable.invokeFunction(method, args);
            result = true;
        } catch (NoSuchMethodException e) {
            exception = e;
        }

        long useTime = System.currentTimeMillis() - startTime;
        if (result) {
            return ScriptResult.success(value, useTime);
        } else {
            return ScriptResult.failure(message, exception, useTime);
        }
    }

    protected abstract String getScriptName();
}
