package message.web.script;

/**
 * 脚本文件解析类.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-17 下午10:01:11
 */
public interface ScriptObjectResolver {
	
	/**
	 * 通过类名解析获取对象(从字节码文件或者其它文件).
	 * 
	 * @param name		包名 + 类名
	 * @return
	 */
	public Object getObject(String name) throws Exception;
}
