package de.sty.io.mimetype.helper;

/**
 * Extends {@link java.lang.NullPointerException} and logs the message.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: NullPointerException.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class NullPointerException extends java.lang.NullPointerException {
	/** Use Log4j to printout messages. */
	protected static Logger logger = Logger.getLogger(NullPointerException.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();
	
	// ----------------------------------------------------------------------

	/** @see java.lang.NullPointerException#NullPointerException() */
	public NullPointerException() {
		super();
		logger.info("new " + this, this);
	}
	/** @see java.lang.NullPointerException#NullPointerException(String) */
	public NullPointerException(String s) {
		super(s);
		logger.info("new " + this, this);
	}
	/** @see Throwable#Throwable(String, Throwable) */
	public NullPointerException(String message, Throwable cause) {
		super(message);
		super.initCause(cause);
		logger.info("new " + this, this);
	}
	/** @see Throwable#Throwable(Throwable) */
	public NullPointerException(Throwable cause) {
		super((cause == null ? "Unknown error." : cause.getMessage()));
		super.initCause(cause);
		logger.info("new " + this, this);
	}
}
