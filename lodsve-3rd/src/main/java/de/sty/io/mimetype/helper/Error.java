package de.sty.io.mimetype.helper;

import java.util.Collection;
import java.util.Iterator;

/**
 * Some methods for Errors and Exceptions.
 * <p>
 * All methods with assert <em>stop</em> the running program via 
 * {@link System#exit(int)}, if the assertion fails!
 * Use the checkXXX-methods instead to get an catchable
 * exception on failure instead. 
 * </p>
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: Error.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class Error {
    /** Use Log4j to printout messages. */
    protected static Logger logger = Logger.getLogger(Error.class);
    /** Shows if we print out debug messages. */
    protected static boolean isDebug = logger.isDebugEnabled();
    
    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    /** Some predefined exitcodes. Exit with error(s). */
    public static final int EXITCODE_ERR = 255;
    /** Some predefined exitcodes. Result is as expected, exit without error. */
    public static final int EXITCODE_OK = 0;
    
    // ----------------------------------------------------------------------
    // Static methods
    // ----------------------------------------------------------------------
    
    public static synchronized String getStackTrace() {
    	StringBuffer sb = new StringBuffer();
    	StackTraceElement[] st = (new RuntimeException()).getStackTrace();
    	for (int i = 1; i < st.length; i++) {
			StackTraceElement e = st[i];
			sb.append("\tat ");
			sb.append(e.toString()).append('\n');
		}
    	return sb.toString();
    }
    
    /** Log the current stacktrace for warning or similar. */
    public static synchronized void logStackTrace() {
    	logger.warn(getStackTrace());
    }
    /** Log the current stacktrace for warning or similar prepending 
     * <code>msg</code> to the log. */
    public static synchronized void logStackTrace(String msg) {
    	logger.warn(msg + '\n' + getStackTrace());
    }
    
    // ----------------------------------------------------------------------
	// Assert 
	// ----------------------------------------------------------------------
    
    /** If <code>ensure != true</code>, 
     * exit this program immediatly. 
     * @deprecated Use assertTrue/assertFalse instead. */
    public static void assertExit(boolean ensure) {
        if (!ensure) errorExit(new AssertionFailedException("Assertion failed."));
    }
    /** If <code>ensure != true</code>, 
     * exit this program immediatly 
     * with the error message given in <code>msg</code>. 
     * @deprecated Use assertTrue/assertFalse instead. */
    public static void assertExit(boolean ensure, String msg) {
    	if (isDebug)
			logger.debug("assertExit(" + ensure + "," + msg + "): called");
        if (!ensure) errorExit(msg, new AssertionFailedException("Assertion failed."));
    }
    
    /** If <code>mustBeTrue != true</code>, 
     * exit this program immediatly. */
    public static void assertTrue(boolean mustBeTrue) {
        if (!mustBeTrue) errorExit(new AssertionFailedException("Assertion failed."));
    }
    /** If <code>mustBeTrue != true</code>, 
     * exit this program immediatly 
     * with the error message given in <code>msg</code>. */
    public static void assertTrue(boolean mustBeTrue, String msg) {
    	if (isDebug)
			logger.debug("assertTrue(" + mustBeTrue + "," + msg + "): called");
        if (!mustBeTrue) errorExit(msg, new AssertionFailedException("Assertion failed."));
    }
    /** If <code>mustBeFalse != false</code>, 
     * exit this program immediatly. */
    public static void assertFalse(boolean mustBeFalse) {
        if (mustBeFalse) errorExit(new AssertionFailedException("Assertion failed."));
    }
    /** If <code>mustBeFalse != false</code>, 
     * exit this program immediatly 
     * with the error message given in <code>msg</code>. */
    public static void assertFalse(boolean mustBeFalse, String msg) {
    	if (isDebug)
			logger.debug("assertFalse(" + mustBeFalse + "," + msg + "): called");
        if (mustBeFalse) errorExit(msg, new AssertionFailedException("Assertion failed."));
    }
    
    // ----------------------------------------------------------------------
	// parameter checks
	// ----------------------------------------------------------------------
    
	/** If <code>mustNotBeNull==null</code>, 
	 * exit this program immediatly. */
	public static void assertNotNull(Object mustNotBeNull) {
		if (mustNotBeNull == null) errorExit(new AssertionFailedException());
	}
	/** If <code>mustNotBeNull==null</code>, 
	 * immediatly exit this program giving <code>msg</code> as reason. */
	public static void assertNotNull(Object mustNotBeNull, String msg) {
		if (mustNotBeNull == null) errorExit(new AssertionFailedException(msg));
	}

	/** If <code>o1</code> do not equals <code>o2</code>
	 * exit this program immediatly 
     * with the error message given in <code>msg</code>. */
	public static void assertEquals(Object o1, Object o2) {
		if((o1 == null && o2 != null) || (o1 != null && o2 == null))
			errorExit(new AssertionFailedException("Not equals: " + o1 + " and " + o2));
		if(o1 != null && !o1.equals(o2))
			errorExit(new AssertionFailedException("Not equals: " + o1 + " and " + o2));
	}
	/** If <code>o1</code> do not equals <code>o2</code>
	 * exit this program immediatly 
     * with the error message given in <code>msg</code>. */
	public static void assertEquals(Object o1, Object o2, String msg) {
		if((o1 == null && o2 != null) || (o1 != null && o2 == null))
			errorExit(msg, new AssertionFailedException("Not equals: " + o1 + " and " + o2));
		if(o1 != null && !o1.equals(o2))
			errorExit(msg, new AssertionFailedException("Not equals: " + o1 + " and " + o2));
	}
	
    // ----------------------------------------------------------------------
    // check parameters for valid content
    // ----------------------------------------------------------------------

	/** If <code>mustNotBeEmpty==null</code> or 
	 * <code>mustNotBeEmpty.equals("")</code> 
	 * exit this program immediatly. */
	public static void assertNotEmpty(String mustNotBeEmpty) {
		try {
			checkNotEmpty(mustNotBeEmpty);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(e));
		}
	}
    /** If <code>c</code> is null or its size < 1
	 * exit this program immediatly. */
    public static void assertNotEmpty(Collection c) {
		try {
			checkNotEmpty(c);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(e));
		}
    }
    /** If <code>o</code> is null or its size < 1
	 * exit this program immediatly. */
    public static void assertNotEmpty(Object[] o) {
		try {
			checkNotEmpty(o);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(e));
		}
    }
    
    // ----------------------------------------------------------------------

    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(Number n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(byte n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(double n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(float n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(int n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    /** If <code>n</code> is null or its value < 0
	 * exit this program immediatly. */
    public static void assertNotNegative(long n) {
		try {
			checkNotNegative(n);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(n + " must not be negative!", e));
		}
    }
    
	// ----------------------------------------------------------------------

    /** If <code>o</code> is not an instance of the class of 
     * <code>compareWith</code>
     * exit this program immediatly. 
	 * If <code>o==null</code> nothing happens.
	 * @param o 	The object to test 
	 * @param compareWith <code>o</code> must be an
	 *						instance of the class of <code>compareWith</code>. */
	public static void assertInstanceOf(Object o, Object compareWith) {
		assertInstanceOf(o, compareWith.getClass());
	}
    /** If <code>o</code> is not an instance of 
     * <code>clazz</code>
     * exit this program immediatly. 
	 * If <code>o==null</code> nothing happens.
	 * 
	 * @param o 	The object to test 
	 * @param clazz <code>o</code> must be an
	 *                       instance of <code>compareWith</code>.
	 * @throws  IllegalArgumentException If it is not an instance of 
	 *                       <code>compareWith</code>. */
	public static void assertInstanceOf(Object o, Class clazz) {
		try {
			checkInstanceOf(o, clazz);
		} catch (Exception e) {
			errorExit(new AssertionFailedException(o + " no instance of "+ clazz, e));
		}
	}

	/** If any object in the {@link Collection} <code>c</code> 
	 * is not an instance of <code>clazz</code>
	 * exit this program immediatly.  
	 * Elements which are <code>null</code> are ignored if
	 * <code>acceptNull==true</code>.
	 * 
	 * @param c The collection to browse
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any element is not 
	 * 					an instance of the class <code>compareWith</code>
	 * 					or is <code>null</code> and <code>acceptNull==false</code>. */
	public static void assertElementsInstanceOf(Collection c, Class clazz, boolean acceptNull) {
		try {
			checkElementsInstanceOf(c, clazz, acceptNull);
		} catch (Exception e) {
			errorExit(new AssertionFailedException("Not all elements of " 
					+ c + " are instances of " + clazz 
					+ " (acceptNull: " + acceptNull, e));
		}
	}

	/** 
	 * If any object in the array <code>objs</code> 
	 * is not an instance of <code>clazz</code>
	 * exit this program immediatly.  
	 * Elements which are <code>null</code> are ignored if
	 * <code>acceptNull==true</code>.
	 * 
	 * @param objs The array to browse
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any element is not 
	 * 					an instance of the class <code>compareWith</code>
	 * 					or is <code>null</code> and <code>acceptNull==false</code>. 
	 */
	public static void assertElementsInstanceOf(Object[] objs, Class clazz, boolean acceptNull) {
		try {
			checkElementsInstanceOf(objs, clazz, acceptNull);
		} catch (Exception e) {
			errorExit(new AssertionFailedException("Not all elements of " 
					+ objs + " are instances of " + clazz 
					+ " (acceptNull: " + acceptNull, e));
		}
	}
	
    /** If <code>c</code> contains a <code>null</code> item or any
     * item is not an instance of <code>compareWith</code>
	 * exit this program immediatly.  
	 * 
	 * @param c The collection to test
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any test fails. */
	public static void assertCollectionProperlyFilled(Collection c, Class clazz) {
	    assertNotNull(c);
	    assertNotNull(clazz);
	    assertNotEmpty(c);
	    assertElementsInstanceOf(c, clazz, false);
	}
    
    // ----------------------------------------------------------------------
	// Catchable checks
	// ----------------------------------------------------------------------

	/** If <code>c==false</code>, 
	 * throw an exception. */
	public static void checkTrue(boolean c) {
		if (!c) throw new RuntimeException();
	}
	/** If <code>c==false</code>, 
	 * throw an exception
	 * giving <code>msg</code> as message. */
	public static void checkTrue(boolean c, String msg) {
		if (!c) throw new RuntimeException(msg);
	}
	/** If <code>c==true</code>, 
	 * throw an exception. */
	public static void checkFalse(boolean c) {
		if (c) throw new RuntimeException();
	}
	/** If <code>c==true</code>, 
	 * throw an exception
	 * giving <code>msg</code> as message. */
	public static void checkFalse(boolean c, String msg) {
		if (c) throw new RuntimeException(msg);
	}
	
	/** If <code>mustNotBeNull==null</code>, 
	 * throw an exception. */
	public static void checkNotNull(Object mustNotBeNull) {
		if (mustNotBeNull == null) throw new NullPointerException();
	}
	/** If <code>mustNotBeNull==null</code>, 
	 * immediatly throw an exception giving <code>msg</code> as reason. */
	public static void checkNotNull(Object mustNotBeNull, String msg) {
		if (mustNotBeNull == null) throw new NullPointerException(msg);
	}
	
	/** If any of the parameters is <code>null</code> 
	 * throw an exception. */
	public static void checkNotNullMult(Object o1, Object o2) {
		if (o1 == null || o2 == null) 
			throw new NullPointerException("One of " 
					+ "'" + o1 + "', '" + o2 + "' is null!");
	}
	/** If any of the parameters is <code>null</code> 
	 * throw an exception. */
	public static void checkNotNullMult(Object o1, Object o2, Object o3) {
		if (o1 == null || o2 == null || o3==null) 
			throw new NullPointerException("One of " 
					+ "'" + o1 + "', '" + o2 + "', '" + o3 + "' is null!");
	}
	/** If any of the parameters is <code>null</code> 
	 * throw an exception. */
	public static void checkNotNullMult(Object o1, Object o2, Object o3, Object o4) {
		if (o1 == null || o2 == null || o3==null || o4==null) 
			throw new NullPointerException("One of " 
					+ "'" + o1 + "', '" + o2 + "', '" + o3 + "', '" + o4 + "' is null!");
	}

	/** If <code>o1</code> do not equals <code>o2</code>
	 * throw an exception. */
	public static void checkEquals(Object o1, Object o2) {
		if( (o1 == null && o2 != null) || (o1 != null && o2 == null) )
			throw new RuntimeException();
		if(o1 != null && !o1.equals(o2))
			throw new RuntimeException();
	}
	
    // ----------------------------------------------------------------------
    // check parameters for valid content
    // ----------------------------------------------------------------------

	/** If <code>mustNotBeEmpty==null</code> or 
	 * <code>mustNotBeEmpty.equals("")</code> 
	 * throw an exception
	 * giving <code>msg</code> as message. */
	public static void checkNotEmpty(String mustNotBeEmpty, String msg) {
		if (mustNotBeEmpty == null || "".equals(mustNotBeEmpty))
			throw new IllegalArgumentException(msg);
	}
	/** If <code>mustNotBeEmpty==null</code> or 
	 * <code>mustNotBeEmpty.equals("")</code> 
	 * throw an exception. */
	public static void checkNotEmpty(String mustNotBeEmpty) {
		checkNotEmpty(mustNotBeEmpty, "String must not be empty!");
	}

	/** If <code>c</code> is null or its size < 1
	 * throw an exception. 
	 * giving <code>msg</code> as message */
    public static void checkNotEmpty(Collection c, String msg) {
        checkNotNull(c, msg);
        if(c.size() < 1)
        	throw new IllegalArgumentException(msg);
    }
	/** If <code>c</code> is null or its size < 1
	 * throw an exception. */
    public static void checkNotEmpty(Collection c) {
        checkNotEmpty(c, "Collection must not be empty!");
    }
    
    /** If <code>o</code> is null or its size < 1
	 * throw an exception. 
	 * giving <code>msg</code> as message */
    public static void checkNotEmpty(Object[] o, String msg) {
        checkNotNull(o, msg);
        if(o.length < 1)
        	throw new IllegalArgumentException(msg);
    }
    /** If <code>o</code> is null or its size < 1
	 * throw an exception. */
    public static void checkNotEmpty(Object[] o) {
        checkNotEmpty(o, "Array must not be empty!");
    }
    
    // ----------------------------------------------------------------------
    
    /** If <code>c</code> is null or contains <code>null</code>
	 * throw an exception. */
    public static void checkNullNotContained(Collection c) {
        checkNotNull(c);
        for (Iterator i = c.iterator(); i.hasNext();) {
			Object obj = (Object) i.next();
			checkNotNull(obj);
		}
    }
    /** If <code>o</code> is null or its size < 1
	 * throw an exception. */
    public static void checkNullNotContained(Object[] o) {
        checkNotNull(o);
        for (int i = 0; i < o.length; i++) {
			Object obj = o[i];
			checkNotNull(obj);
		}
    }
    
    // ----------------------------------------------------------------------

    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(Number n) {
        checkNotNull(n);
        if(n.longValue() < 0 || n.doubleValue() < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(byte n) {
        if(n < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(double n) {
        if(n < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(float n) {
        if(n < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(int n) {
        if(n < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    /** If <code>n</code> is null or its value < 0
	 * throw an exception. */
    public static void checkNotNegative(long n) {
        if(n < 0)
        	throw new IllegalArgumentException("Number must not be negative: " + n);
    }
    
	// ----------------------------------------------------------------------

    /** If <code>o</code> is not an instance of the class of 
     * <code>compareWith</code>
     * throw an exception.
	 * If <code>o==null</code> nothing happens.
	 * @param o 	The object to test 
	 * @param compareWith <code>o</code> must be an
	 *						instance of the class of <code>compareWith</code>. */
	public static void checkInstanceOf(Object o, Object compareWith) {
		checkInstanceOf(o, compareWith.getClass());
	}
    /** If <code>o</code> is not an instance of 
     * <code>clazz</code>
     * throw an exception.
	 * If <code>o==null</code> nothing happens.
	 * 
	 * @param o 	The object to test 
	 * @param clazz <code>o</code> must be an
	 *                       instance of <code>compareWith</code>.
	 * @throws  IllegalArgumentException If it is not an instance of 
	 *                       <code>compareWith</code>. */
	public static void checkInstanceOf(Object o, Class clazz) {
	    checkNotNull(clazz);
		if (o != null && !clazz.isInstance(o)) {
			String sOClass = o.getClass().getName();
			throw new IllegalArgumentException("Incompatible class. "
					+ "Object '" + o + "', instance of '" + sOClass + "', " 
					+ " is not an instance of '" + clazz.getName() + "'");
		}
	}

	/** If any object in the {@link Collection} <code>c</code> 
	 * is not an instance of <code>clazz</code>
	 * throw an exception.
	 * Elements which are <code>null</code> are ignored if
	 * <code>acceptNull==true</code>.
	 * 
	 * @param c The collection to browse
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any element is not 
	 * 					an instance of the class <code>compareWith</code>
	 * 					or is <code>null</code> and <code>acceptNull==false</code>. */
	public static void checkElementsInstanceOf(Collection c, Class clazz, boolean acceptNull) {
		checkNotNull(c);
		checkNotNull(clazz);
		
		Iterator i = c.iterator();
		Object o;
		while (i.hasNext()) {
			o = i.next();
			// accept null entries?
			if(acceptNull && o == null)
			    continue;
			
			if (!clazz.isInstance(o)) {
				String oType = (o == null ? "NULL" : o.getClass().getName());
				throw new IllegalArgumentException("Incompatible class in collection. '"
						+ oType + "' is not of type '"
							+ clazz.getName() + "'");
			}
		}
	}
	/** If any object in the array <code>objs</code> 
	 * is not an instance of <code>clazz</code>
	 * throw an exception.
	 * Elements which are <code>null</code> are ignored if
	 * <code>acceptNull==true</code>.
	 * 
	 * @param objs The array to browse
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any element is not 
	 * 					an instance of the class <code>compareWith</code>
	 * 					or is <code>null</code> and <code>acceptNull==false</code>. */
	public static void checkElementsInstanceOf(Object[] objs, Class clazz, boolean acceptNull) {
		checkNotNull(objs);
		checkNotNull(clazz);
		
		for (int i = 0; i < objs.length; i++) {
            Object o = objs[i];

			// accept null entries?
			if(acceptNull && o == null)
			    continue;
			
			if (!clazz.isInstance(o)) {
				String oType = (o == null ? "NULL" : o.getClass().getName());
				throw new IllegalArgumentException("Incompatible class in collection. '"
						+ oType + "' is not of type '"
							+ clazz.getName() + "'");
			}
		}
	}
	
    /** If <code>c</code> contains a <code>null</code> item or any
     * item is not an instance of <code>compareWith</code>
	 * throw an exception.
	 * 
	 * @param c The collection to test
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any test fails. */
	public static void checkCollectionProperlyFilled(Collection c, Class clazz) {
	    checkNotNull(c);
	    checkNotNull(clazz);
	    checkNotEmpty(c);
	    checkElementsInstanceOf(c, clazz, false);
	}
    /** If <code>objs</code> contains a <code>null</code> item or any
     * item is not an instance of <code>compareWith</code>
	 * throw an exception.
	 * 
	 * @param objs The array to test
	 * @param clazz Each element from <code>c</code> must be an
	 *                       instance of this class.
	 * @throws IllegalArgumentException If any test fails. */
	public static void checkArrayProperlyFilled(Object[] objs, Class clazz) {
	    checkNotNull(objs);
	    checkNotNull(clazz);
	    checkNotEmpty(objs);
	    checkElementsInstanceOf(objs, clazz, false);
	}
	
	// ----------------------------------------------------------------------
	// Special test for Strings
	// ----------------------------------------------------------------------

    /** If <code>o</code> is no instance of 
     * <code>String</code> or <code>o==null</code>
	 * throw an exception
	 * giving <code>msg</code> as message */
    public static void checkIsString(Object o, String msg) {
    	if(!(o instanceof String))
    		throw new IllegalArgumentException(msg);
    }
    /** If <code>o</code> is no instance of 
     * <code>String</code> or <code>o==null</code> 
	 * throw an exception. */
    public static void checkIsString(Object o) {
        checkIsString(o, "Must be instance of String and not be empty!");
    }

	/** If <code>o==null</code> or <code>o</code> is no instance of 
     * <code>String</code> or <code>o=""</code>
	 * throw an exception
	 * giving <code>msg</code> as message */
    public static void checkIsStringAndNotEmpty(Object o, String msg) {
    	if(o instanceof String) {
            checkNotEmpty((String) o, msg);
    	} else {
    		throw new IllegalArgumentException(msg);
    	}
    }
    /** If <code>o==null</code> or <code>o</code> is no instance of 
     * <code>String</code> or <code>o=""</code>
	 * throw an exception. */
    public static void checkIsStringAndNotEmpty(Object o) {
        checkIsStringAndNotEmpty(o, "Must be instance of String and not be empty!");
    }
    
    // ----------------------------------------------------------------------
	// Hard exit
	// ----------------------------------------------------------------------
	
    /** Exit with an error. */
    public static void errorExit() {
        errorExit("Unexpected Application Failure!");
    }
    /** Exit with an error, printing <code>msg</code> to stderr. 
     * @param msg The Message to print on stderr. */
    public static void errorExit(String msg) {
        errorExit(msg, null);
    }
    /** Exit with an error, printing <code>e</code>'s Message to stderr. 
     * @param e Use {@link Throwable#getLocalizedMessage()} to print 
     *          a message on stderr. */
    public static void errorExit(Throwable e) {
        errorExit(e.getLocalizedMessage(), e);
    }    
    /** Exit with an error, printing <code>msg</code> and <code>e</code>'s Message to stderr. 
     * @param msg Printed to sterr.
     * @param e Use {@link Throwable#getLocalizedMessage()} to print 
     *          a message on stderr. */
    public static void errorExit(String msg, Throwable e) {
        // create default Throwable if no one given
        if (e == null) 
            e = new Exception("Unknown Error.");
        
        logger.error(msg, e);
        System.err.println("\n\n" + "========== Unexpected Application Failure! ==========");
        System.err.println();
        System.err.println("Begin of stack trace:");
        System.err.println("---------------------");
        e.printStackTrace();
        System.err.println("------------------");
        System.err.println("End of stack trace");
        System.err.println();
        System.err.println("An unrecoverable error occured. Sorry! :(");
        System.err.println();
        System.err.println("Technical Message: " + e.getClass() + ": " + e.getMessage());
        System.err.println("  Possible Reason: " + e.getLocalizedMessage());
        System.err.println("    Error Message: " + msg);
        System.err.println();
        System.exit(EXITCODE_ERR);
    }
    
    // ----------------------------------------------------------------------
	// Exceptions
	// ----------------------------------------------------------------------
    
    /** 
     * Throws a new {@link RuntimeException} wrapping 
     * the given exception.
     * @param e The wrapped exeception
     * @throws RuntimeException 
     */ 
    public static void rethrow(Exception e) {
    	logger.info("Rethrowing exception." + '\n' + getStackTrace());
    	throw new RuntimeException(e);
    }
    /** 
     * Throws a new {@link RuntimeException} wrapping 
     * the given exception.
     * @param msg A message for the thrown exception
     * @param e The wrapped exeception
     * @throws RuntimeException 
     */ 
    public static void rethrow(String msg, Exception e) {
    	logger.info(msg + '\n' + getStackTrace());
    	throw new RuntimeException(msg, e);
    }
    
	// ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------

    /** Normally not used. */
    protected Error() {
        super();
    }
    

}