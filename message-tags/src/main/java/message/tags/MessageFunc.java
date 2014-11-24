package message.tags;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义的jstl中的fn函数,对集合等的操作.
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-7-29 下午05:41:18
 */
public class MessageFunc {
	private static final Logger logger = LoggerFactory.getLogger(MessageFunc.class);
	
	/**
	 * 判断一个对象是否在另一个集合中
	 * 
	 * @param descArray
	 * @param srcArray
	 * @return
	 */
	public static boolean contains(List<Object> descArray, Object srcArray){
		if(descArray == null){
			logger.debug("descArray is null, return false");
			return false;
		}
		if(srcArray == null){
			logger.debug("srcArray is null, return true");
			return true;
		}
		
		return descArray.contains(srcArray);
	}
	
	/**
	 * 返回一个array的长度
	 * 
	 * @param obj
	 * @return
	 */
	public static int length(Object obj){
		if(obj == null){
			return 0;
		}
		
		if(obj instanceof Collection){
			return ((List) obj).size();
		} else if(obj instanceof Object[]){
			return ((Object[]) obj).length;
		} else if(obj instanceof Map){
			return ((Map) obj).size();
		} else {
			throw new NestableRuntimeException("参数类型不正确！");
		}
	}
}
