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
package lodsve.scripts.python;

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
 * @date 2018-1-2-0002 10:40
 */
public class PythonScriptEngineTest {
    @Test
    public void testPython() throws ScriptException {
        ScriptEngine python = DynamicScriptEngineFactory.getEngine(Script.PYTHON);
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
