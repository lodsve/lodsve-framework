package de.sty.io.mimetype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.sty.io.mimetype.helper.Error;
import de.sty.io.mimetype.helper.Helper;

/**
 * This class contains a glob. 
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: GlobImpl.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class GlobImpl implements MimeType.Glob {
		
	/** The filename glob/pattern. */		
	String glob;
		
	protected boolean endPattern;
	protected boolean startPattern;
	protected boolean exactPattern;
	protected String pat;
	protected transient Matcher m;

	// ----------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------
	
	/** Create a new empty instance. */
	public GlobImpl() {
		super();
	}

	/** Create a new instance with the given value. */
	GlobImpl(String glob) {
		super();
		setGlob(glob);
	}
	
	// ----------------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------------
	
	void setGlob(String glob) {
		Error.checkNotNull(glob);

		if(glob.lastIndexOf('*') == 0) {
			endPattern = true;
			pat = glob.substring(1);
		} else if(glob.indexOf('*') == glob.length()) {
			startPattern = true;
			pat = glob.substring(0, glob.length() - 2);
		} else if(glob.indexOf('?') < 0 && glob.indexOf('*') < 0) {
			exactPattern = true;
		} else {
			// build proper regex
			// escape regex special chars
			pat = Helper.convertGlobPattern2Regex(glob);
			// compile regex
			m = Pattern.compile(pat, 
				Pattern.CASE_INSENSITIVE 
				| Pattern.UNICODE_CASE
				| Pattern.CANON_EQ 
				| Pattern.DOTALL).matcher("");
		}
		this.glob = glob;
	} // end setGlob(String)

	// ----------------------------------------------------------------------

	/** @see MimeType.Glob#getGlob() */
	public String getGlob() {
		return glob;
	}

	/** @see MimeType.Glob#matches(String) */
	public boolean matches(String name) {
		Error.checkNotNull(name);
		boolean ret = false;
		if(endPattern) {
			ret = name.endsWith(pat);
		} else if(startPattern) {
			ret = name.startsWith(pat);
		} else if(exactPattern) {
			ret = name.equals(glob);
		} else {
			ret = m.reset(name).matches();
		}
		return ret;
	}
	
	// ----------------------------------------------------------------------
	
	public String toString() {
		return "<glob pattern=\"" + glob + "\"/>";
	}
	
	/** @see Object#equals(Object) */
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof GlobImpl) {
			GlobImpl g = (GlobImpl) o;
			ret = ((glob == null && g.glob == null) || glob.equals(g.glob));
		}
		return ret;
	}

} // end GlobImpl
