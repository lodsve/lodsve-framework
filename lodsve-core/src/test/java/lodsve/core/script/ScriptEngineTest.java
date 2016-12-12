package lodsve.core.script;

import javax.script.ScriptException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/12 上午9:44
 */
public class ScriptEngineTest {
    public static void main(String[] args) throws ScriptException {
        ScriptEngine groovy = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.GROOVY);
        ScriptEngine js = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.JS);
        ScriptEngine python = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.PYTHON);
        ScriptEngine spel = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.SPEL);
        ScriptEngine ruby = DynamicScriptEngineFactory.getEngine(DynamicScriptEngineFactory.Script.RUBY);


        String groovyCode1 = "1+2";
        String groovyCode2 = "String testGroovy(arg1,arg2) {return arg1+arg2;}";

        String jsCode1 = "1+2";
        String jsCode2 = "function testJs(arg1,arg2) {return arg1+arg2;}";

        String pythonCode1 = "1+2";
        String pythonCode2 = "def testPython( str ):\n\treturn str + \" World!\";";

        String spelCode1 = "2+3";

        String rubyCode1 = "2+19";
        String rubyCode2 = "def testRuby (arg1, arg2)\n\tresult = arg1 + arg2\nreturn result\nend";

        boolean compileGroovyCode1 = groovy.compile("groovyCode1", groovyCode1);
        boolean compileGroovyCode2 = groovy.compile("groovyCode2", groovyCode2);
        System.out.println("compileGroovyCode1=" + compileGroovyCode1 + "\ncompileGroovyCode1=" + compileGroovyCode2);
        System.out.println(groovy.execute("groovyCode1"));
        System.out.println(groovy.invoke("groovyCode2", "testGroovy", 1, 2));

        System.out.println("===================");

        boolean compileJsCode1 = js.compile("jsCode1", jsCode1);
        boolean compileJsCode2 = js.compile("jsCode2", jsCode2);
        System.out.println("compileJsCode1=" + compileJsCode1 + "\ncompileJsCode2=" + compileJsCode2);
        System.out.println(js.execute("jsCode1"));
        System.out.println(js.invoke("jsCode2", "testJs", 1, 2));

        System.out.println("===================");

        boolean compilePythonCode1 = python.compile("pythonCode1", pythonCode1);
        boolean compilePythonCode2 = python.compile("pythonCode2", pythonCode2);
        System.out.println("compilePythonCode1=" + compilePythonCode1 + "\ncompilePythonCode2=" + compilePythonCode2);
        System.out.println(python.execute("pythonCode1"));
        System.out.println(python.invoke("pythonCode2", "testPython", "Hello"));

        System.out.println("===================");

        boolean compileSpelCode = spel.compile("spelCode1", spelCode1);
        System.out.println("compileSpelCode=" + compileSpelCode);
        System.out.println(spel.execute("spelCode1"));

        System.out.println("===================");

        boolean compileRubyCode1 = ruby.compile("rubyCode1", rubyCode1);
        boolean compileRubyCode2 = ruby.compile("rubyCode2", rubyCode2);
        System.out.println("compileRubyCode1=" + compileRubyCode1 + "\ncompileRubyCode2=" + compileRubyCode2);
        System.out.println(ruby.execute("rubyCode1"));
        System.out.println(ruby.invoke("rubyCode2", "testRuby", 1, 2));
    }
}
