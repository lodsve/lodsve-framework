package de.sty.io.mimetype.helper;

/**
 * Extends {@link java.lang.RuntimeException} and logs the message.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: AssertionFailedException.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class AssertionFailedException extends java.lang.RuntimeException {
	/** Use Log4j to printout messages. */
	protected static Logger logger = Logger.getLogger(AssertionFailedException.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();
	
	// ----------------------------------------------------------------------

	/** @see java.lang.RuntimeException#RuntimeException() */
	public AssertionFailedException() {
		super("Internal Error (an assertion failed)!");
		logger.error("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(String) */
	public AssertionFailedException(String s) {
		super("Internal Error (an assertion failed): " + s);
		logger.error("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(Throwable) */
	public AssertionFailedException(Throwable cause) {
		this(cause.getLocalizedMessage(), cause);
		logger.error("new " + this, this);
	}
	/** @see java.lang.RuntimeException#RuntimeException(String, Throwable) */
	public AssertionFailedException(String message, Throwable cause) {
		super("Internal Error (an assertion failed): " + message, cause);
		logger.error("new " + this, this);
	}
}
