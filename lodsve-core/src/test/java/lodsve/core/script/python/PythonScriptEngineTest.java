package lodsve.core.script.python;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-2-0002 10:40
 */
public class PythonScriptEngineTest {
    @Test
    public void testPython() throws ScriptException {
        ScriptEngine python = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.PYTHON);
        String pythonCode1 = "1+2";
        String pythonCode2 = "def testPython( str ):\n\treturn str + \" World!\";";

        boolean compilePythonCode1 = python.compile("pythonCode1", pythonCode1);
        boolean compilePythonCode2 = python.compile("pythonCode2", pythonCode2);

        Assert.assertTrue(compilePythonCode1);
        Assert.assertTrue(compilePythonCode2);

        Assert.assertEquals("3", python.execute("pythonCode1").toString());
        Assert.assertEquals("Hello World!", python.invoke("pythonCode2", "testPython", "Hello").toString());
    }
}
