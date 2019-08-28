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

package lodsve.core.script.ruby;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 10:46
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
