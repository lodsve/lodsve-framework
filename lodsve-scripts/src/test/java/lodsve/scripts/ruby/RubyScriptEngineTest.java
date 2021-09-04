/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.scripts.ruby;

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
 * @date 2018-1-2-0002 10:46
 */
public class RubyScriptEngineTest {
    @Test
    public void testRuby() throws ScriptException {
        ScriptEngine ruby = DynamicScriptEngineFactory.getEngine(Script.RUBY);
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
