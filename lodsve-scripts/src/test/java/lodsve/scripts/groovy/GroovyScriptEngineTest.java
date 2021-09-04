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
package lodsve.scripts.groovy;

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
 * @date 2018-1-2-0002 10:34
 */
public class GroovyScriptEngineTest {
    @Test
    public void testGroovy() throws ScriptException {
        ScriptEngine groovy = DynamicScriptEngineFactory.getEngine(Script.GROOVY);
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
