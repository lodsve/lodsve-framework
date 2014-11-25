package message.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对date类型的操作
 * @author sunhao(sunhao.java@gmail.com)
 */
public class DateFormat {
	SimpleDateFormat dateFormat;
	
	public DateFormat(String pattern){
		this.dateFormat = new SimpleDateFormat(pattern);
	}
	
	public String format(Date value){
		return this.dateFormat.format(value);
	}
	
	public static String format(Date value, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(value);
	}
	
	public Date parse(String text) throws ParseException{
		Date result = null;
		try{
			result = this.dateFormat.parse(text);
		} catch(ParseException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static Date parse(String text, String pattern) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(text);
	}
	
	public static Date strToDate(String strDate, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static Date strToDateTime(String strDateTime, String fromat){
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(fromat);
		Date dateTime = null;
		try {
			dateTime = dateTimeFormat.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateTime;
	}
	
}
