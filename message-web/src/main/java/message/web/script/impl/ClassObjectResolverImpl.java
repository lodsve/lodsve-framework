package message.web.script.impl;

import message.utils.StringUtils;

/**
 * 解析Java字节码文件为对象的实现类.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-17 下午10:12:26
 */
public class ClassObjectResolverImpl extends AbstractScriptObjectResolver {

	public Object getObject(String name) throws Exception {
		return super.getObjectFromScript(name);
	}

	/**
	 * 实现Java字节码文件包名+类名到路径的解析
	 */
	protected String prepareName(String name) {
		if(name.endsWith(".class")){
			name = StringUtils.removeEnd(name, ".class");
		}
		String result = StringUtils.replaceChars(name, ".", "\\");
		
		return result + ".class";
	}
}
