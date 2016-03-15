package de.sty.io.mimetype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.sty.io.mimetype.helper.Helper;

/**
 * 
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: MagicImpl.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class MagicImpl implements MimeType.Magic {
		
	ArrayList matches = new ArrayList();
	int prio;

	// ----------------------------------------------------------------------
	// Constructor 
	// ----------------------------------------------------------------------		
	
	MagicImpl() {
		super();
	}
	
	MagicImpl(int prio) {
		super();
		this.prio = prio;
	}

	// ----------------------------------------------------------------------
	
	boolean eA(byte[] b1, byte[] b2) {
		boolean ret = false;
		if(b1 == null)
			ret = (b2 == null); 
		else {
			if(b2 != null && b1.length == b2.length) {
				ret = true;
				for(int i = 0; ret && i < b1.length; i++)
					ret = (b1[i] == b2[i]);
			}
		}
		return ret;
	}
	
	int typeToType(String type) {
		int ret = Match.TYPES.length - 1;
		while(ret > 0 && !Match.TYPES[ret].equalsIgnoreCase(type))
			ret--;
		return ret;
	}
	String typeToType(int type) {
		return Match.TYPES[type];
	}
	
	MatchImpl addMatchImpl(int type, int offsetStart, int offsetEnd, byte[] value, byte[] mask) {
//		for (Iterator i = matches.iterator(); i.hasNext();) {
//			MatchImpl m = (MatchImpl) i.next();
//			if(m.getType() == type 
//					&& m.getOffsetStart() == offsetStart
//					&& m.getOffsetEnd() == offsetEnd 
//					&& eA(m.getValue(), value)
//					&& eA(m.getMask(), mask))
//				return m;
//		}
		MatchImpl ret;
		if(mask==null) 
			ret = new MatchImpl(type, offsetStart, offsetEnd, value);
		else
			ret = new MatchImpl(type, offsetStart, offsetEnd, value, mask);
		matches.add(ret);
		return ret;
	}

		
	// ----------------------------------------------------------------------
	// Methods 
	// ----------------------------------------------------------------------

	/** @see MimeType.Magic#getMatches() */
	public Collection getMatches() {
		return matches;
	}

	/** @see MimeType.Magic#getPrio() */
	public int getPrio() {
		return prio;
	}

	/** @see MimeType.Magic#matches(byte[]) */
	public boolean matches(byte[] buffer) {
		boolean ret = false;
		for (Iterator i = matches.iterator(); !ret && i.hasNext();) {
			Match m = (Match) i.next();
			ret = m.matches(buffer);
		}
		return ret;
	}

	/** @see MimeType.Magic#matchLength() */
	public int matchLength() {
		int ret = 0;
		for (Iterator i = matches.iterator(); i.hasNext();) {
			MatchImpl m = (MatchImpl) i.next();
			int l = m.offsetEnd + m.value.length;
			ret = (l > ret ? l : ret);
		}
		return ret;
	}
	
	// ----------------------------------------------------------------------
	
	public String toString() {
		StringBuffer ret = new StringBuffer("<magic priority=\"");
		ret.append(this.prio).append("\">");
		for (Iterator i = matches.iterator(); i.hasNext();) {
			Match mt = (Match) i.next();
			ret.append(mt);
		}
		ret.append("</magic>");
		return ret.toString();
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof MagicImpl) {
			MagicImpl m = (MagicImpl) o;
			ret = (prio == m.prio);
			if(ret) 
				ret = Helper.equals(matches, m.matches);
		}
		return ret;
	}


} // end MagicImpl


