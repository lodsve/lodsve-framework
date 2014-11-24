package message.web.script;

import javassist.ClassPool;
import javassist.CtClass;
import message.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 使用脚本解析controller的工具类.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-17 下午09:58:13
 */
public class ScriptUtils {
	private final static Logger logger = LoggerFactory.getLogger(ScriptUtils.class);
	
	/**
	 * 根据脚本文件名解析出对象
	 * 
	 * @param scriptFile		脚本文件名
	 * @return
	 * @throws Exception
	 */
	public static Object getObject(String scriptFile) throws Exception {
		if(scriptFile.endsWith(".class")){
			return resolveClassObject(scriptFile);
		}
		
		return null;
	}

	/**
	 * 解析Java字节码文件为对象
	 * 
	 * @param scriptFile
	 * @return
	 * @throws Exception
	 */
	private static Object resolveClassObject(String scriptFile) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		InputStream is = new FileInputStream(new File(scriptFile));
		CtClass ct = cp.makeClass(is);
		ct.setName(ct.getName() + "_" + RandomUtils.getRandomNum(5));
		Class clazz = ct.toClass();
		
		logger.debug("get class '{}' for class file '{}'", clazz, scriptFile);
		return BeanUtils.instantiateClass(clazz);
	}
}
