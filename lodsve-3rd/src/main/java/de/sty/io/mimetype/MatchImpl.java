package de.sty.io.mimetype;

import java.util.Collection;
import java.util.Iterator;

import de.sty.io.mimetype.helper.ByteHelper;
import de.sty.io.mimetype.helper.Error;

/**
 * A match is part of a {@link MagicImpl} and can contain
 * sub-matches.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: MatchImpl.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class MatchImpl extends MagicImpl implements MimeType.Magic.Match {

	/** Not really used -- magic values are translated into byte[] */
	int type;
	/** The offset from wihich on the value must match the stream. */
	int offsetStart;
	int offsetEnd;
	
	/** The value to compare the stream against. */
	byte[] value;
	/** Possibly mask the value with AND before comparing the result
	 * with the stream. */
	byte[] mask;
	/** If <code>&</code> apply the mask before the comparision,
	 * if <code>=</code> compare the value directly. */
	char op;
			
	// ----------------------------------------------------------------------
	
	/** Create a new Match without mask. */
	public MatchImpl(int type, int offsetStart, int offsetEnd, byte[] value) {
		super();
		if(type<TYPE_STRING || type > TYPE_BYTE)
			throw new IllegalArgumentException("Unknown type: " + type);
		if(offsetStart < 0 || offsetEnd < 0 || offsetEnd < offsetStart)
			throw new IllegalArgumentException("Wrong offsets: " + offsetStart + "-" + offsetEnd);

		Error.checkNotNull(value, "value must not be null!");

		this.type = type;
		this.offsetStart = offsetStart;
		this.offsetEnd = offsetEnd;
		this.value = value;
		// no mask, use equal
		op = '=';
	}
	/** Create a new Match with mask. */
	public MatchImpl(int type, int offsetStart, int offsetEnd, byte[] value, byte[] mask) {
		super();
		if(type<TYPE_STRING || type > TYPE_BYTE)
			throw new IllegalArgumentException("Unknown type: " + type);
		if(offsetStart < 0 || offsetEnd < 0 || offsetEnd < offsetStart)
			throw new IllegalArgumentException("Wrong offsets: " + offsetStart + "-" + offsetEnd);
		Error.checkNotNull(value, "value must not be null!");
		Error.checkNotNull(mask, "mask must not be null!");
		if(value.length != mask.length)
			throw new IllegalArgumentException("value and mask must have the same length!");
					
		this.type = type;
		this.offsetStart = offsetStart;
		this.offsetEnd = offsetEnd;
		this.value = value;
		this.mask = mask;
		op = '&';
	}

	// ----------------------------------------------------------------------
						
	/** @see Match#getMask() */
	public byte[] getMask() {
		// return copy, no reference
		byte[] ret = new byte[mask.length];
		System.arraycopy(mask, 0, ret, 0, mask.length);
		return ret;
	}

	/** @see Match#getType() */
	public int getType() {
		return this.type;
	}

	/** @see Match#getOffsetStart() */
	public int getOffsetStart() {
		return this.offsetStart;
	}
	/** @see Match#getOffsetEnd() */
	public int getOffsetEnd() {
		return this.offsetEnd;
	}

	/** @see Match#getValue() */
	public byte[] getValue() {
		// return copy, no reference
		byte[] ret = new byte[value.length];
		System.arraycopy(value, 0, ret, 0, value.length);
		return ret;
	}

	/** @see Match#getMatches() */
	public Collection getMatches() {
		return matches;
	}

	/** @see MimeType.Magic#matchLength() */
	public int matchLength() {
		if(value == null)
			throw new IllegalStateException("value == null: class not initialized!");
		int ret = offsetEnd + value.length;
		for (Iterator i = matches.iterator(); i.hasNext();) {
			MatchImpl m = (MatchImpl) i.next();
			int l = m.matchLength();
			ret = (l > ret ? l : ret);
		}
		return ret;
	}

	/** @see Match#matches(byte[]) */
	public boolean matches(byte[] buffer) {
		boolean ret = false;
		if(buffer == null || value == null || buffer.length < offsetStart + value.length)
			return false;
			
		// _any_ match on this level must match, not all! 
		// search first matching match 
		// compare buffer until match or offsetEnd reached  

		// t runs from offsetStart to offsetEnd
		int t = 0;
		do {
			boolean bH = true;
			for (int i = 0, o = offsetStart + t; bH && o < buffer.length && i < value.length; i++, o++) {
				bH = test(buffer[o], i);
			}
			ret = bH;
			t++;
		// without match try 
		} while (ret == false && t < offsetEnd - offsetStart && offsetStart + t + value.length < buffer.length);
		if(ret) {
			//  submatches must match also
			for (Iterator i = matches.iterator(); ret && i.hasNext();) {
				Match m = (Match) i.next();
				ret = m.matches(buffer);
			}
		}
		return ret;
	}
	boolean test(byte testbyte, int i) {
		boolean ret = false;
		if(op == '=')
			ret = (testbyte == value[i]);
		else
			ret = (((byte) (testbyte & mask[i])) == value[i]);
		return ret;
	}

	// ----------------------------------------------------------------------

	public String toString() {
		StringBuffer ret = new StringBuffer("<match type=\"");
		ret.append(typeToType(this.type)).append('\"');
		ret.append(" offset=\"").append(this.offsetStart).append(":");
		ret.append(this.offsetEnd).append('\"');
		if(value != null) {
			ret.append(" value=\"");
			// append values without 0 and special chars as text
			byte[] minmax = ByteHelper.getMinMax(value);
			if(mask == null && minmax[0] > 0)
				ret.append(new String(value));
			else
				ret.append('0').append('x').append(ByteHelper.toHexString(value));
			ret.append('\"');
		}
		if(mask != null) { 
			ret.append(" mask=\"");
			ret.append('0').append('x').append(ByteHelper.toHexString(mask));
			ret.append('\"');
		}
		ret.append('>');
		
		for (Iterator i = matches.iterator(); i.hasNext();) {
			Match mt = (Match) i.next();
			ret.append(mt);
		}
		ret.append("</match>");
		return ret.toString();
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof MatchImpl) {
			MatchImpl m = (MatchImpl) o;
			ret = (type == m.type)
				&& (offsetStart == m.offsetStart)
				&& (op == m.op)
				&& ((value == null && m.value == null) || value.equals(m.value))
				&& ((mask == null && m.mask == null) || mask.equals(m.mask));
			if(ret) ret = super.equals(o);
		}
		return ret;
	}
} // end MatchImpl
