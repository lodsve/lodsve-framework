package message.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 进行字符串的格式化,
 * 格式化字符串，替换{0}{1}...
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-14 下午08:26:53
 */
public class ReplaceStringUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReplaceStringUtils.class);
	
	/**
	 * 左括号
	 */
	private final static String LEFT_BRACE = "{";
	/**
	 * 右括号
	 */
	private final static String RIGTH_BRACE = "}";

    /**
     * 私有化构造器
     */
    private ReplaceStringUtils(){}
	
	/**
	 * 格式化字符串，替换{0}{1}...，如果不成功，则默认返回原字符串
	 * 
	 * @param formatString	需要格式化的字符串
	 * @param args			参数
	 * @return
	 */
	public static String replace(String formatString, Object[] args){
		String result = replace(formatString, args, StringUtils.EMPTY);
		
		return StringUtils.isEmpty(result) ? formatString : result;
	}
	
	/**
	 * 格式化字符串，替换{0}{1}...，如果不成功，则返回defaultVlaue
	 * 
	 * @param formatString	需要格式化的字符串
	 * @param args			参数
	 * @param defaultValue	如果不成功，返回的字符串
	 * @return
	 */
	public static String replace(String formatString, Object[] args, String defaultValue){
		/*if(!(StringUtils.isEmpty(formatString) || !(formatString.indexOf(LEFT_BRACE) == -1 || formatString.indexOf(RIGTH_BRACE) == -1)
				|| args == null || args.length < 1)){
			logger.error("the formatString, args, defaultValue is required! please check given paramters are all right!");
			return StringUtils.EMPTY;
		}*/
		
		if(StringUtils.isEmpty(formatString) || formatString.indexOf(LEFT_BRACE) == -1 || formatString.indexOf(RIGTH_BRACE) == -1
				|| args == null || args.length < 1){
			logger.error("the formatString, args, defaultValue is required! please check given paramters are all right!");
			return StringUtils.EMPTY;
		}
		
		/**
		 * 第一个'{'的位置
		 */
		int firstLeftBrace = formatString.indexOf(LEFT_BRACE);
		/**
		 * 最后一个'}'的位置
		 */
		int lastRightBrace = formatString.lastIndexOf(RIGTH_BRACE);
		/**
		 * 第一个'{'后面的那个数字
		 */
		String first = formatString.substring(firstLeftBrace + 1, firstLeftBrace + 2);
		/**
		 * 最后一个'}'后面的那个数字
		 */
		String last = formatString.substring(lastRightBrace - 1, lastRightBrace);
		
		/**
		 * 第一个序号和最后一个序号
		 */
		int firstSequence = Integer.parseInt(first);
		int lastSequence = Integer.parseInt(last);
		
		/**
		 * {0}{1}...的个数与给定的值个数不一致，或者{0}{1}...不是按照这样递增的，那么返回错误,
		 * 否则进行替换
		 */
		if(!((lastSequence - firstSequence + 1) < 0 || (lastSequence - firstSequence + 1) != args.length)){
			/**
			 * 替换规则：
			 * 给定值的第一个值替换{0}，以此类推
			 */
			for(int j = 0; j < args.length; j++){
				formatString = formatString.replace("{" + j + "}", args[j] + "");
			}
		} else {
			if(logger.isWarnEnabled()){
				logger.warn("foramtString has '{}' barces, but you given '{}' paramters!", 
						lastSequence - firstSequence + 1, args.length);
			}
			
			return defaultValue;
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("format successed! the result is '{}'", formatString);
		}
		
		return formatString;
	}
}
