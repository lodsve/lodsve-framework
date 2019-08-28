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

package lodsve.core.script.groovy;

import lodsve.core.script.DynamicScriptEngineFactory;
import lodsve.core.script.ScriptEngine;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 10:34
 */
public class GroovyScriptEngineTest {
    @Test
    public void testGroovy() throws ScriptException {
        ScriptEngine groovy = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.GROOVY);
        String groovyCode1 = "1+2";
        String groovyCode2 = "String testGroovy(arg1,arg2) {return arg1+arg2;}";

        boolean compileGroovyCode1 = groovy.compile("groovyCode1", groovyCode1);
        boolean compileGroovyCode2 = groovy.compile("groovyCode2", groovyCode2);

        Assert.assertTrue(compileGroovyCode1);
        Assert.assertTrue(compileGroovyCode2);

        Assert.assertEquals("3", groovy.execute("groovyCode1").toString());
        Assert.assertEquals("3", groovy.invoke("groovyCode2", "testGroovy", 1, 2).toString());
    }
}
