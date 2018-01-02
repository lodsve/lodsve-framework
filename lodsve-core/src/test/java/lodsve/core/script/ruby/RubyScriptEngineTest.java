package lodsve.core.script.ruby;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-2-0002 10:46
 */
public class RubyScriptEngineTest {
    @Test
    public void testRuby() throws ScriptException {
        ScriptEngine ruby = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.RUBY);
        String rubyCode1 = "2+19";
        String rubyCode2 = "def testRuby (arg1, arg2)\n\tresult = arg1 + arg2\nreturn result\nend";

        boolean compileRubyCode1 = ruby.compile("rubyCode1", rubyCode1);
        boolean compileRubyCode2 = ruby.compile("rubyCode2", rubyCode2);

        Assert.assertTrue(compileRubyCode1);
        Assert.assertTrue(compileRubyCode2);

        Assert.assertEquals("21", ruby.execute("rubyCode1").toString());
        Assert.assertEquals("3", ruby.invoke("rubyCode2", "testRuby", 1, 2).toString());
    }
}
