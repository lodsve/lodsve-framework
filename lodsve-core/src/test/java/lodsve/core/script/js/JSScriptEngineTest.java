package lodsve.core.script.js;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 10:37
 */
public class JSScriptEngineTest {
    @Test
    public void testJS() throws ScriptException {
        ScriptEngine js = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.JS);
        String jsCode1 = "parseInt(1+2)";
        String jsCode2 = "function testJs(arg1,arg2) {return parseInt(arg1+arg2);}";

        boolean compileJsCode1 = js.compile("jsCode1", jsCode1);
        boolean compileJsCode2 = js.compile("jsCode2", jsCode2);

        Assert.assertTrue(compileJsCode1);
        Assert.assertTrue(compileJsCode2);

        Assert.assertNotNull(js.execute("jsCode1").getResult());
        Assert.assertNotNull(js.invoke("jsCode2", "testJs", 1, 2).getResult());
    }
}
