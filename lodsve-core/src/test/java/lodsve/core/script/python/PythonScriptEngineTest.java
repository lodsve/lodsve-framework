/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.core.script.python;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 10:40
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
