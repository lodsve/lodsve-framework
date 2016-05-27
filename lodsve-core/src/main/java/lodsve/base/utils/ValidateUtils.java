package lodsve.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具栏
 * 
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-7-12 下午04:09:14
 */
public class ValidateUtils {
	
	//邮箱正则表达式
	private static final String REG_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	//固话正则表达式
	private static final String REG_PHONE = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
	//手机号码正则表达式
	private static final String REG_MOBILE = "0{0,1}(13[4-9]|15[7-9]|15[0-2]|18[7-9])[0-9]{8}";
	//url正则表达式
	private static final String REG_URL = "^((https|http|ftp|rtsp|mms)://)?[A-Za-z0-9]+\\.[A-Za-z0-9]+[\\/=\\?%\\-&_~`@\\':+!]*([^<>\\\"\\\"])*$";
	//身份证号码正则表达式
	private static final String REG_IDCARD = "\\d{15}|\\d{18}";
	//是否是数字的正则表达式
	private static final String REG_NUMBER = "\\d+";
	//邮编的正则表达式
	private static final String REG_ZIP = "^[1-9]\\d{5}$";
	//QQ
	private static final String REG_QQ = "[1-9]\\d{4,13}";
	//整数
	private static final String REG_INTEGER = "[-\\+]?\\d+";
	//小数
	private static final String REG_DOUBLE = "[-\\+]?\\d+(\\.\\d+)?";
	//英文
	private static final String REG_ENGLISH = "^[A-Za-z]+$";
	//中文
	private static final String REG_CHINESE = "^[\\u0391-\\uFFE5]+$";
	//IP
	private static final String REG_IP = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";

    /**
     * 私有化构造器
     */
    private ValidateUtils(){}

    /**
	 * 判断是否是邮箱地址
	 * 
	 * @param email		待验证的邮箱地址
	 * @return
	 */
	public static boolean isEmail(String email){
		//不区分大小写
        Pattern pattern = Pattern.compile(REG_EMAIL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        
        /**
         * 注意find与matches()的区别
         * 调用find方法，只执行尽量匹配
         * 而调用matches方法，则执行严格的匹配
         */
		return StringUtils.isNotEmpty(email) && matcher.matches();
	}
	
	/**
	 * 判断是否是电话号码,电话(固话)
	 * 
	 * @param phone		固话号码
	 * @return
	 */
	public static boolean isPhone(String phone){
		Pattern patter = Pattern.compile(REG_PHONE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(phone);
		
		return StringUtils.isNotEmpty(phone) && matcher.matches();
	}
	
	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobile	手机号码
	 * @return
	 */
	public static boolean isMobile(String mobile){
		Pattern patter = Pattern.compile(REG_MOBILE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(mobile);
		
		return StringUtils.isNotEmpty(mobile) && matcher.matches();
	}
	
	/**
	 * 判断是否是正确的url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url){
		Pattern patter = Pattern.compile(REG_URL, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(url);
		
		return StringUtils.isNotEmpty(url) && matcher.matches();
	}
	
	/**
	 * 判断是否是合法的身份证号码
	 * 
	 * @param idCard	身份证号码
	 * @return
	 */
	public static boolean isIdCard(String idCard){
		Pattern patter = Pattern.compile(REG_IDCARD, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(idCard);
		
		return StringUtils.isNotEmpty(idCard) && matcher.matches();
	}
	
	/**
	 * 判断是否是数字
	 * 
	 * @param number	数字
	 * @return
	 */
	public static boolean isNumber(String number){
		Pattern patter = Pattern.compile(REG_NUMBER, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(number);
		
		return StringUtils.isNotEmpty(number) && matcher.matches();
	}
	
	/**
	 * 判断邮编是否合法
	 * 
	 * @param zip		邮编
	 * @return
	 */
	public static boolean isZip(String zip){
		Pattern patter = Pattern.compile(REG_ZIP, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(zip);
		
		return StringUtils.isNotEmpty(zip) && matcher.matches();
	}
	
	/**
	 * 判断QQ号是否合法
	 * 
	 * @param qq
	 * @return
	 */
	public static boolean isQq(String qq){
		Pattern patter = Pattern.compile(REG_QQ, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(qq);
		
		return StringUtils.isNotEmpty(qq) && matcher.matches();
	}
	
	/**
	 * 判断是否是整数 
	 * 
	 * @param integer
	 * @return
	 */
	public static boolean isInteger(String integer){
		Pattern patter = Pattern.compile(REG_INTEGER, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(integer);
		
		return StringUtils.isNotEmpty(integer) && matcher.matches();
	}
	
	/**
	 * 判断是否是小数
	 * 
	 * @param double_
	 * @return
	 */
	public static boolean isDouble(String double_){
		Pattern patter = Pattern.compile(REG_DOUBLE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(double_);
		
		return StringUtils.isNotEmpty(double_) && matcher.matches();
	}
	
	/**
	 * 判断是否是英文
	 * 
	 * @param english
	 * @return
	 */
	public static boolean isEnglish(String english){
		Pattern patter = Pattern.compile(REG_ENGLISH, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(english);
		
		return StringUtils.isNotEmpty(english) && matcher.matches();
	}
	
	/**
	 * 判断QQ号是否合法
	 * 
	 * @param chinese
	 * @return
	 */
	public static boolean isChinese(String chinese){
		Pattern patter = Pattern.compile(REG_CHINESE, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(chinese);
		
		return StringUtils.isNotEmpty(chinese) && matcher.matches();
	}
	
	/**
	 * 判断IP是否合法
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIp(String ip){
		Pattern patter = Pattern.compile(REG_IP, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patter.matcher(ip);
		
		return StringUtils.isNotEmpty(ip) && matcher.matches();
	}
}
