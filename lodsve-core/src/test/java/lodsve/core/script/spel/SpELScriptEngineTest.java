package lodsve.core.script.spel;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 10:43
 */
public class SpELScriptEngineTest {
    @Test
    public void testSPEL() throws ScriptException {
        ScriptEngine spel = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.SPEL);
        String spelCode1 = "2+3";

        boolean compileSpelCode = spel.compile("spelCode1", spelCode1);

        Assert.assertTrue(compileSpelCode);
        Assert.assertEquals("5", spel.execute("spelCode1").toString());
    }
}
