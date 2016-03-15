package de.sty.io.mimetype.helper;

/**
 * Extends {@link java.lang.RuntimeException} and logs the message.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: RuntimeException.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class RuntimeException extends java.lang.RuntimeException {
	/** Use Log4j to printout messages. */
	protected static Logger logger = Logger.getLogger(RuntimeException.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();
	
	// ----------------------------------------------------------------------

	/** @see java.lang.RuntimeException#RuntimeException() */
	public RuntimeException() {
		super();
		logger.info("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(String) */
	public RuntimeException(String s) {
		super(s);
		logger.info("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(String, Throwable) */
	public RuntimeException(String message, Throwable cause) {
		super(message, cause);
		logger.info("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(Throwable) */
	public RuntimeException(Throwable cause) {
		super(cause);
		logger.info("new " + this, this);
	}
}
