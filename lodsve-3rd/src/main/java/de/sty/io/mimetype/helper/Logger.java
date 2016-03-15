package de.sty.io.mimetype.helper;

/**
 * Simple logger class. 
 *
 * @author <a href="http://www.stuelten.de">Timo St&uuml;ten</a>
 * @version $Id: Logger.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class Logger {
	
	// ----------------------------------------------------------------------
	// Constants
	// ----------------------------------------------------------------------
	
	public static Logger getLogger(Class clz) {
		Logger ret = new Logger();
		ret.prefix = clz.toString();
		
		return ret;
	}
	
	// ----------------------------------------------------------------------
	// 
	// ----------------------------------------------------------------------
	
	protected String prefix;
	
	public void error(String msg) {
		System.out.println(System.currentTimeMillis()
				+ " ERROR [" + prefix + "] " + msg);
	}
	public void warn(String msg) {
		System.out.println(System.currentTimeMillis()
				+ " WARN [" + prefix + "] " + msg);
	}
	public void info(String msg) {
		System.out.println(System.currentTimeMillis()
				+ " INFO [" + prefix + "] " + msg);
	}
	public void debug(String msg) {
//		System.out.println(System.currentTimeMillis()
//				+ " DEBUG [" + prefix + "] " + msg);
	}
	
	public void error(String msg, Object o) {
		System.out.println(System.currentTimeMillis()
				+ " ERROR [" + prefix + "] " + msg + " " + o);
	}
	public void warn(String msg, Object o) {
		System.out.println(System.currentTimeMillis()
				+ " WARN [" + prefix + "] " + msg + " " + o);
	}
	public void info(String msg, Object o) {
		System.out.println(System.currentTimeMillis()
				+ " INFO [" + prefix + "] " + msg + " " + o);
	}
	public void debug(String msg, Object o) {
//		System.out.println(System.currentTimeMillis()
//				+ " DEBUG [" + prefix + "] " + msg + " " + o);
	}
	
	public boolean isDebugEnabled() {
		return true;
	}
	public boolean isInfoEnabled() {
		return true;
	}
	
}
