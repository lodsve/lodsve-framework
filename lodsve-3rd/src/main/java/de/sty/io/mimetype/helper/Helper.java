package de.sty.io.mimetype.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;

/**
 * Contains some helping methods.
 *
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: Helper.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class Helper {
	
	static Logger logger = Logger.getLogger(Helper.class);
	
	/** Special chars for regular expressions. */
	public static final String[] REGEX_ESCAPE_PATTERN = { "\\", "[", "]", "(", ")", "^", "$", "+", "|", "." };
	
	/** The directory where to store serialized data. */
	public static final String STORAGE_DIRNAME = "de.sty";
	
	/** The currently running OS. */
	public static final String OSNAME = System.getProperty("os.name");
	
	// ----------------------------------------------------------------------
	// Compare, Equals, Collections 
	// ----------------------------------------------------------------------
	
	/** Roughly compare two objects, in nearly the way 
	 * {@link Comparable#compareTo(Object)} would do
	 * but do not throw {@link ClassCastException}s or 
	 * {@link NullPointerException}s if both given objects do not share a
	 * common class or equals <code>null</code>.
	 * <p>
	 * If any object is <code>null</code>, no exception
	 * is thrown. Additionally, if both objects are of incompatible classes,
	 * no {@link ClassCastException} is thrown, as {@link Comparable}
	 * would do. Instead their string representation is compared.
	 * </p><p>
	 * If <code>o1</code> implements {@link Comparable}, its 
	 *  {@link Comparable#compareTo(Object)} method is used.
	 * If both objects are numbers, their long or double value is compared.
	 * Otherwise the string representation of both objects is compared.
	 * </p><p>
	 * <em>This method does NOT behave exactly as
	 * {@link Comparable#compareTo(Object)} would do.</em>
	 * It is useful to roughly compare (or sort) objects.  
	 * </p>
	 * @param o1 An object
	 * @param o2 An object
	 * @return This method returns 
	 * 		<code>0</code> if <code>o1==null</code> and <code>o2==null</code>,
	 * 		if <code>o1==o2</code>, or <code>o1.equals(o2)</code>.
	 * 		<br/> 
	 * 		It returns {@link Integer#MAX_VALUE}, if only <code>o1==null</code>, and 
	 * 		{@link Integer#MIN_VALUE}, if only <code>o2==null</code>.
	 * 		<br/>
	 * 		The result of <code>o1.compareTo(o2)</code> is returned, if both
	 * 		objects implement {@link Comparable} and <code>o2</code> is
	 * 		an instance of the class of <code>o1</code>.
	 * 		The difference of their {@link Number#longValue() long value} is returned, 
	 * 		if both objects are instances 
	 * 		of {@link Number} and the difference is not <code>0</code>. Otherwise 
	 * 		their {@link Number#doubleValue() double values} are used and 
	 * 		the result from {@link Double#compare(double, double)} is returned.
	 * 		<br/>
	 * 		If both objects are not directly comparable this way, their 
	 * 		{@link Object#toString() string representation} is used and this method 
	 * 		returns the result of
	 * 		<code>o1.toString().compareTo(o2.toString())</code>.
	 **/
	public static int compare(Object o1, Object o2) {
		int ret = Integer.MAX_VALUE;
		if (o1 == null || o2 == null) {
			if (o1 == null && o2 == null)
				ret = 0;
			else
				if(o1 == null)
					ret = Integer.MAX_VALUE;
				else
					ret = Integer.MIN_VALUE;
		} else {
			if(o1 == o2 || o1.equals(o2)) {
				// simple case
				ret = 0;
			} else if(o1 instanceof Comparable && o2 instanceof Comparable
					&& o1.getClass().isInstance(o2)) {
				// both are of the same class and 
				// are directly comparable
				ret = ((Comparable) o1).compareTo(o2);
			} // methods below must not return 0
			// as o1 and o2 are not directly comparable
			else if(o1 instanceof Number && o2 instanceof Number) {
				// both are numbers 
				// as the ususal numbers are comparable
				// this code will not get called very often
				long l1 = ((Number) o1).longValue();
				long l2 = ((Number) o2).longValue();
				ret = (int) (l1 - l2);
				if(ret == 0) { 
					double d1 = ((Number) o1).doubleValue();
					double d2 = ((Number) o2).doubleValue();
					ret = Double.compare(d1, d2); // 0 only if d1 exact equals d2
					if(ret == 0)
						// Do not return 0, as both objects are not equal.
						// Compare classnames to get stable return values.
						// Use 2/-2 to avoid clashes with 1/-1 computed 
						// above by Double.compare 
						ret = (o1.getClass().getName().compareTo(o2.getClass().getName()) < 0
								? -2 : 2 );
				}
			} else {
				// At least compare the objects' string representation.
				ret = o1.toString().compareTo(o2.toString());
				if(ret == 0)
					// Do not return 0, as both objects are not equal.
					// Compare classnames to get stable return values.
					// Use 2/-2 to avoid clashes with 1/-1 computed 
					// above by String.compareTo 
					ret = (o1.getClass().getName().compareTo(o2.getClass().getName()) < 0
							? -2 : 2 );
			}
		}
		return ret;
	}
	
	/** Tests <code>o1</code> and <code>o2</code> for equality with 
	 * respect to null values. 
	 * @return <code>true</code> if both <code>o1</code> and <code>o2</code>  
	 *         are <code>null</code>. Otherwise returns the result from 
	 *         <code>o1.equals(o2)</code>. */
	public static boolean eq(Object o1, Object o2) {
		boolean ret = false;
		if (o1 == null)
			if (o2 == null)
				ret = true;
			else
				ret = false;
		else
			ret = o1.equals(o2);
		return ret;
	}
	
	/** Tests <code>c1</code> and <code>c2</code> for equality with 
	 * respect to null values. 
	 * @return <code>true</code> if both <code>c1</code> and <code>c2</code>  
	 *         are <code>null</code> or all elements are equal and 
	 * 			are in the same order. */
	public static boolean equals(Collection c1, Collection c2) {
		boolean ret = false;
		if (c1 == null || c2 == null) {
			if (c1 == null && c2 == null)
				ret = true;
			else
				ret = false;
		} else {
			ret = (c1.size() == c2.size());
			if(ret) {
				// if same size, compare every element
				Iterator i1 = c1.iterator();
				Iterator i2 = c2.iterator();
				// i2 has same size as i1: 
				// checking of i2.hasNext() is not necessary 
				while(ret && i1.hasNext()) 
					ret = Helper.eq(i1.next(), i2.next());
			}
		}
		return ret;
	}
	
	/** Insert all elements from <code>src</code> to <code>dest</code> and throw
	 * an exception, if any element is no instance of <code>allowed</code>.
	 * Calls {@link Collection#add(Object) dest.add(Object)}
	 * for every element of <code>src</code>. 
	 * If <code>src==null</code> nothing happens.
	 * @return <code>true</code> if <code>dest</code> was changed
	 */
	public static boolean addAll(Collection src, Collection dest, Class allowed) {
		Error.assertNotNull(allowed);
		Error.assertNotNull(dest);
		boolean ret = false;
		if (src != null) {
			Iterator i = src.iterator();
			Object o;
			while (i.hasNext()) {
				o = i.next();
				if (!allowed.isInstance(o)) {
					String oType =
						(o == null ? "NULL" : o.getClass().getName());
					throw new RuntimeException(
						"Incompatible class in collection."
							+ oType
							+ " is not of type "
							+ allowed.getName());
				}
				ret = ret | dest.add(o);
			}
		}
		return ret;
	}
	
	// ----------------------------------------------------------------------
	// Strings
	// ----------------------------------------------------------------------
	
	/** Converts a shell pattern or glob into a regex pattern. 
	 * Escapes all special chars.
	 *  @param glob The pattern or glob to convert, e.g. <code>"*.txt"</code>
	 * @return A regular expression, like <code>".*\.txt"</code>
	 */
	public static String convertGlobPattern2Regex(String glob) {
		// build proper regex
		String pat = glob;
		// escape regex special chars
		for(int iH = 0; iH < REGEX_ESCAPE_PATTERN.length; iH++) {
			pat = replace(pat, 
					REGEX_ESCAPE_PATTERN[iH], "\\" + REGEX_ESCAPE_PATTERN[iH]);
		}
		// convert simple glob pattern to regex
		pat = replace(pat, "*", ".*");
		pat = replace(pat, "?", ".");
		
		return pat;
	}
	
	/** Returns <code>text</code> with all occurences of 
	 * <code>search</code> replaced by <code>replace</code>. 
	 * Replacing is not recursive: replacing <code>a</code>
	 * with <code>aa</code> in <code>XaaX</code> produces
	 * <code>XaaaaX</code>. 
	 * @param text the string to search in.
	 * @param search the string to search for.
	 * @param replace the string to replace <code>search</code> with.
	 * @return a new string with every occurence of 
	 * 				<code>search</code> replaced. */
	public static String replace(String text, String search, String replace) {
		int iH = text.indexOf(search);
		if (iH < 0) 
			// search not found
			return text;
		
		// prepare for at least one replace 
		StringBuffer ret; 
		ret = new StringBuffer(text.length() + replace.length() - search.length());
		int iPos = 0;
		int iSearchLen = search.length();
		while (iH >= 0) { // while search found
			// use head and replace-text
			ret.append(text.substring(iPos, iH)).append(replace);
			// append trailing text behind search 
			iPos = iH + iSearchLen;
			iH = text.indexOf(search, iPos);
		}
		// append trailing text
		if (iPos < text.length()) ret.append(text.substring(iPos));
		
		return ret.toString();
	}
	
	// ----------------------------------------------------------------------
	// I/O
	// ----------------------------------------------------------------------

	/**
	 * Return the current dir.
	 * @return <code>new File(System.getProperty("user.dir"))</code>
	 */
	public static File getCurrentDir() {
		return new File(System.getProperty("user.dir"));
	}
	/**
	 * Return the system's temp dir.
	 * @return <code>new File(System.getProperty("java.io.tmpdir"))</code>
	 */
	public static File getTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}
	/**
	 * Return the user's home dir.
	 * @return <code>new File(System.getProperty("user.home"))</code>
	 */
	public static File getUserHome() {
		return new File(System.getProperty("user.home"));
	}

	/** @return the dir used by default to store persistent data.
	 * 			It is hidden and In the user's file space. */
	public static File getStorageDir() {
		File userhome = new File(System.getProperty("user.home"));
		File ret = new File(userhome, STORAGE_DIRNAME); 
		ret = createHiddenDirectory(ret.getAbsolutePath());
		Error.assertNotNull(ret, "Cannot create hidden storage dir " + STORAGE_DIRNAME);
		if(!ret.exists()) ret.mkdirs();
		Error.assertTrue(ret.exists(), "Cannot create directory " + ret + " for persistent storage.");
		return ret;		
	}

	/** Create a new file. */
	public static File createFromURL(URL url) {
	    Error.checkNotNull(url);
	    
	    File ret = null;
	    if(!"file".equals(url.getProtocol())) 
	        throw new IllegalArgumentException("Cannot convert to file: " + url);
	    try {
		    String fileenc = System.getProperty("file.encoding");
		    String filename = URLDecoder.decode(url.getPath(), fileenc);
		    ret = new File(filename);
	    } catch (UnsupportedEncodingException uee) {
            // should never happen
            Error.errorExit("Unexpected Exception caught.", uee);
        }
		return ret;
	}
	
	/** Create a new hidden directory. The name may change on Unix. 
	 * If the directory already exists, that directory is returned without creating
	 * a new one.. */
	public static File createHiddenDirectory(String pathname) {
		File ret = null;
		if(OSNAME.indexOf("Win") >= 0) {
			ret = new File(pathname);
			if(!ret.exists())
				ret.mkdirs();
			// set attribute via external process
			String[] exec = { "attrib", "+h", pathname };
			Runtime r = Runtime.getRuntime();
			Process p = null;
			try {
				// Execute command
				p = r.exec(exec);
				// wait for process to finish
				p.waitFor();
			} catch (Exception e) {
				p = null;
				throw new RuntimeException("Cannot create hidden directory " 
						+ pathname, e);
			}
		} else {
			// usually unix like: use . as prefix
			String name = getParent(pathname) 
			+ File.separatorChar;
			String filename = getName(pathname);
			if(!filename.startsWith("."))
				name += '.';
			name += filename; 
			ret = new File(name);
			// create dir if it does not exist
			if(!ret.exists())
				ret.mkdirs();
		}
		return ret;
	}
	/** Create a new hidden directory. The name may change on Unix. */
	public static File createHiddenDirectory(File file) {
		File ret = createHiddenDirectory(file.getAbsolutePath());
		return ret;
	}
	
	/**
	 * Get the parent path from filename.
	 * @return "/some/path/" for "/some/path/test.txt"
	 * @see File#getParent()
	 */
	private static String getParent(String filename) {
		Error.checkNotNull(filename);
		String ret = null;
		String arg = filename.replace('\\', '/');
		int iPos = arg.lastIndexOf('/');
		if(iPos >= 0)
			ret = arg.substring(0, iPos).replace('/', File.separatorChar);
		return ret;
	}
	
	/**
	 * Get the filename from a path. That is the remaining part in path after the last
	 * {@link File#separatorChar}, slash or backslash.
	 * This is different from {@link File#getName()}
	 * @return "test.txt" for "/some/path/test.txt" and "\some\path\test.txt"
	 * 		depending on {@link File#separatorChar}.
	 * @see File#getName()
	 */
	private static String getName(String filepath) {
		Error.checkNotNull(filepath);
		String ret = null;
		// Use every slash as seperator
		String arg = filepath; 
		arg = arg.replace('\\', File.separatorChar);
		arg = arg.replace('/', File.separatorChar);
		int iPos = arg.lastIndexOf(File.separatorChar);
		if(iPos >= 0)
			ret = arg.substring(++iPos);
		return ret;
	}
	
	// ----------------------------------------------------------------------
	
	/**
	 * Write <code>text</code> into <code>file</code>. Ignore all errors.
	 * @param file The file to write to.
	 * @param text The String to write.
	 * @return <code>true</code> if an error occured.
	 */
	public static boolean writeFile(File file, String text) {
		Error.checkNotNullMult(file, text);
		boolean ret = false;
		OutputStream out = openOut(file);
		BufferedOutputStream bout = null;
		try {
			out = new FileOutputStream(file);
			bout = new BufferedOutputStream(out);
			bout.write(text.getBytes());
		} catch (IOException ioe) {
			logger.error("Exception while writing file " + file, ioe);
			ret = true;
		}
		Helper.close(bout);
		Helper.close(out);
		return ret;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Open an {@link BufferedInputStream} for a file.
	 * @param file the file to open
	 * @return The opened stream
	 * @throws RuntimeException if an error occurs
	 */
	public static BufferedInputStream openIn(File file) {
		Error.checkNotNull(file);
		BufferedInputStream ret = null;
		if(file.isDirectory())
			throw new RuntimeException("Cannot open directory! " + file);
		try {
			ret = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		return ret;
	}
	/**
	 * Open an {@link BufferedOutputStream} for a file.
	 * @param file the file to open
	 * @return The opened stream or <code>null</code> if the file
	 * 			cannot be opened.
	 * @throws RuntimeException if an error occurs
	 */
	public static BufferedOutputStream openOut(File file) {
		Error.checkNotNull(file);
		if(file.isDirectory())
			throw new RuntimeException("Cannot open directory! " + file);		
		FileOutputStream fout = null;
		BufferedOutputStream ret = null;
		try {
			fout = new FileOutputStream(file);
			ret = new BufferedOutputStream(fout);
		} catch (FileNotFoundException fnfe) {
			Helper.close(ret);
			Helper.close(fout);
			throw new RuntimeException(fnfe);
		}
		return ret;
	}
	/** Close the stream, ignoring any exceptions. Useful for finally-blocks. */
	public static void close(InputStream in) {
		if(in != null) {
			try {
				in.close();
			} catch (Exception e) {
				logger.warn("Ignoring exception while closing stream: " + e);
			}
		}
	}
	/** Close the stream, ignoring any exceptions. Useful for finally-blocks. */
	public static void close(OutputStream out) {
		if(out != null) {
			try {
				out.close();
			} catch (Exception e) {
				logger.warn("Ignoring exception while closing stream: " + e);
			}
		}
	}

}
