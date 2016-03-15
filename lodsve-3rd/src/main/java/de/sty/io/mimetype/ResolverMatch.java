package de.sty.io.mimetype;

/**
 * The result of a mimetype pattern or magic match for a file or stream.  
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: ResolverMatch.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class ResolverMatch implements Comparable {
	
	/** The name of this class. Only for {@link #toString()}. */
	protected static final String CLASSNAME = ResolverMatch.class.getName();
	
	/** The priority of this match. */
	int prio;
	/** The resulting mimetypoe. */
	MimeType mimetype;
	
	// ----------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------
	
	/** Create a new empty instance. */
	public ResolverMatch() {
		super();
	}
	/** Create a new instrance with the given values. */
	ResolverMatch(int prio, MimeTypeImpl mt) {
		this();
		this.prio = prio;
		this.mimetype = mt; 
	}

	/** @return The {@link #mimetype}. */
	public MimeType getMimetype() {
		return mimetype;
	}

	/** @return The {@link #prio}. */
	public int getPrio() {
		return prio;
	}
	
	// ----------------------------------------------------------------------

	/** @see Comparable#compareTo(Object) */
	public int compareTo(Object o) {
		int ret = Integer.MIN_VALUE;
		if (o instanceof ResolverMatch) {
			ResolverMatch m = (ResolverMatch) o;
			ret = prio - m.prio;
		}
		return ret;
	}
	
	public String toString() {
		return prio + ":" + mimetype.getName();
	}

}
