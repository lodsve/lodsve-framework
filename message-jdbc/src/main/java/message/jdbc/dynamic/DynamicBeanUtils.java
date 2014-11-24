package message.jdbc.dynamic;

import javassist.ClassPool;
import javassist.LoaderClassPath;

/**
 * dynamic bean create util
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:56:03
 */
public class DynamicBeanUtils {
	
	public final static ClassPool classPool;
	
	static {
		//TODO
		ClassPool parent = ClassPool.getDefault();
		ClassPool child = new ClassPool(parent);
		child.appendClassPath(new LoaderClassPath(DynamicRowMapper.class.getClassLoader()));
//        child.appendClassPath(new LoaderClassPath(ControllerProxy.class.getClassLoader()));
        child.appendSystemPath(); // the same class path as the default one.
        child.childFirstLookup = true; // changes the behavior of the child.
        classPool = child;
	}
	
	public static String createMapperKey(Class clazz, String sql) {
		StringBuffer key = new StringBuffer();
		key.append(sql)
			.append("-")
			.append(clazz.getName());
		
		return key.toString();
	}
	
	/**
	 * 将类中的字段转成数据库中的字段
	 * 格式：pkId --> pk_id
	 * 
	 * @param name
	 * @return
	 */
	public static String underscoreName(String name) {
		StringBuffer result = new StringBuffer();
		if(name != null && name.length() > 0){
			for(int i = 0; i < name.length(); i++){
				String tmp = name.substring(i, i + 1);
				//判断截获的字符是否是大写，大写字母的toUpperCase()还是大写的
				if(tmp.equals(tmp.toUpperCase())){
					//此字符是大写的
					result.append("_").append(tmp.toLowerCase());
				} else {
					result.append(tmp);
				}
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 将数据库中的字段转成类中的字段
	 * 格式：pk_id --> pkId
	 * 
	 * @param name
	 * @return
	 */
	public static String decodeUnderscoreName(String name){
		StringBuffer result = new StringBuffer();
		boolean up = false;
		if(name != null && name.length() > 0){
			//转成小写
			name = name.toLowerCase();
			for(int i = 0; i < name.length(); i++){
				//取得i位置的字符
				String tmp = name.substring(i, i + 1);
				if("_".equals(tmp)) {
					up = true;
					//结束本次循环，进入下次循环
					continue;
				}
				
				if(up) {
					result.append(tmp.toUpperCase());
					up = false;
				} else {
					result.append(tmp);
				}
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param clazzs
	 * @param sql
	 * @param forMap
	 * @return
	 */
	public static String createMapperKey(Class[] clazzs, String sql, boolean forMap) {
		StringBuffer result = new StringBuffer();
		result.append(sql);
		result.append(forMap ? "true" : "false");
		result.append("-");
		for(Class c : clazzs){
			result.append(c.getName());
		}
		
		return result.toString();
	}

}
