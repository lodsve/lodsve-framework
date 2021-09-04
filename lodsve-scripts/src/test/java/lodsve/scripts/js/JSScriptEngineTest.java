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
