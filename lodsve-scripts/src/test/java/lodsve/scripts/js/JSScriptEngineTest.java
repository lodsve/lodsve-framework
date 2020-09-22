/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.scripts.js;

import lodsve.scripts.DynamicScriptEngineFactory;
import lodsve.scripts.Script;
import lodsve.scripts.ScriptEngine;
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
        ScriptEngine js = DynamicScriptEngineFactory.getEngine(Script.JS);
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
