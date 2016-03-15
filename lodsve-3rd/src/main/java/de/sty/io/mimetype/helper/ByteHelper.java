package de.sty.io.mimetype.helper;

/**
 * Helper methods for byte handling.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: ByteHelper.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
/* Created on Sep 1, 2003. */
public class ByteHelper {

	// ----------------------------------------------------------------------
	// conversion methods
	// ----------------------------------------------------------------------   

	/** An byte as a string bit representation.
	 * @return <code>b</code> as binary String (e.g. "10110111"). */
	public static String toBinString(byte b) {
		return toBinString((int) b);
	}
	/** An int as a string bit representation.
	 * @return <code>i</code> as binary String (e.g. "10110111"). */
	public static String toBinString(int i) {
		boolean bT = ((i & 128) == 128);
		String s = ( bT ? "1" : "0");
		bT = ((i & 64) == 64);
		s += ( bT ? "1" : "0");
		bT = ((i & 32) == 32);
		s += ( bT ? "1" : "0");
		bT = ((i & 16) == 16);
		s += ( bT ? "1" : "0");
		bT = ((i & 8) == 8);
		s += ( bT ? "1" : "0");
		bT = ((i & 4) == 4);
		s += ( bT ? "1" : "0");
		bT = ((i & 2) == 2);
		s += ( bT ? "1" : "0");
		bT = ((i & 1) == 1);
		s += ( bT ? "1" : "0");
		return s;
	}
	/** A String's bit representation as byte array. 
	 * @return <code>s</code> as byte array. (e.g. "0000010100110111":  [5][103]). */
	public static byte[] binStringToByte(String s) {
		byte[] buffer = new byte[(s.length() + 7)/8];
		int iPos = buffer.length - 1;
		while(!s.equals("")) {
			int start = (s.length() > 8 ? s.length() - 8 : 0);
			String sH = s.substring(start, s.length());
			buffer[iPos--] = binStringToSingleByte(sH);
			s = s.substring(0, start);
		}
		return buffer;
	}
	
	/** A String's bit representation as byte.
	 * @return <code>s</code> as byte. (e.g. "00110111":  [103]). */
	public static byte binStringToSingleByte(String s) {
		int iByte = 0;
		int iH = 0;
		while(iH < 8 && iH < s.length()) {
			// shift left one bit
			iByte <<= 1;
			char c = s.charAt(iH++);
			iByte |= (c=='1' ? 1 : 0);
		}
		return (byte) iByte;
	}

	/** A String's hex representation as byte array. 
	 * @return <code>s</code> as byte array. (e.g. "0a37":  [10][103]). */
	public static byte[] hexStringToByte(String s) {
		byte[] buffer = new byte[(s.length() + 1)/2];
		int iPos = buffer.length - 1;
		while(!s.equals("")) {
			int start = (s.length() > 2 ? s.length() - 2 : 0);
			String sH = s.substring(start, s.length());
			buffer[iPos--] = hexStringToSingleByte(sH);
			s = s.substring(0, start);
		}
		return buffer;
	}
	/** A String's bit representation as byte.
	 *  @return <code>s</code> as byte. (e.g. "0f":  [15]). */
	public static byte hexStringToSingleByte(String s) {
		int iByte = 0;
		int iPos = 0;
		while(iPos < 2 && iPos < s.length()) {
			iByte <<= 4;
			iByte |= nibbleCharToByte(s.charAt(iPos++));
		}
		return (byte) iByte;
	}

	/** The value of the char containing an hex-char (0-9, a-f)
	 * as byte.
	 * @return The value of the char containing an hex-char (0-9, a-f)
	 * as byte.
	 * (e.g. <code>0xd3</code> will return <code>"3"</code>). */
	public static byte nibbleCharToByte(char c) {
		byte ret = 0;
		switch (c) {
			case '0' : ret = 0; break;
			case '1' : ret = 1; break;
			case '2' : ret = 2; break;
			case '3' : ret = 3; break;
			case '4' : ret = 4; break;
			case '5' : ret = 5; break;
			case '6' : ret = 6; break;
			case '7' : ret = 7; break;
			case '8' : ret = 8; break;
			case '9' : ret = 9; break;
			case 'A':
			case 'a' : ret = 10; break;
			case 'B':
			case 'b' : ret = 11; break;
			case 'C':
			case 'c' : ret = 12; break;
			case 'D':
			case 'd' : ret = 13; break;
			case 'E':
			case 'e' : ret = 14; break;
			case 'F':
			case 'f' : ret = 15; break;
		}
		return ret;
	}

	/** @return The value of the lower 4 bits as hex string
	 * (e.g. <code>0xd3</code> will return <code>"3"</code>). */
	public static String nibbleAsString(byte b) {
		String s = "";
		b = (byte) (b & 15);
		if (b < 10) {
			s = "" + b;
		} else {
			switch (b) {
				case 10 : s = "a"; break;
				case 11 : s = "b"; break;
				case 12 : s = "c"; break;
				case 13 : s = "d"; break;
				case 14 : s = "e"; break;
				case 15 : s = "f"; break;
			}
		}
		return s;
	}
	/** @return The value of <code>b</code> as hex string
	 * (e.g. <code>0xd3</code> will return <code>"d3"</code>). */
	public static String toHexString(byte b) {
		StringBuffer sb = new StringBuffer("00");
		sb.append(Integer.toHexString(b));
		return sb.substring(sb.length() - 2);
	}
	/** @return The value of <code>i</code> as hex string
	 * (e.g. <code>0xd3</code> will return <code>"d3"</code>). */
	public static String toHexString(int i) {
		StringBuffer sb = new StringBuffer("00000000");
		sb.append(Integer.toHexString(i));
		return sb.substring(sb.length() - 8);
	}
	/** @return The value of <code>i</code> as hex string
	 * (e.g. <code>0xd3</code> will return <code>"d3"</code>). */
	public static String toHexString(long l) {
		StringBuffer sb = new StringBuffer("0000000000000000");
		sb.append(Long.toHexString(l));
		return sb.substring(sb.length() - 16);
	}
	/** @return The value of <code>bytes</code> as hex string
	 * with the lowest index left. 
	 * <code>bytes[0] = 0xab</code> and <code>bytes[1] = 0xcd</code>  
	 * will return <code>"abcd"</code>. */
	public static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int iH = 0; iH < bytes.length; iH++)
			sb.append(toHexString(bytes[iH]));
		return sb.toString();
	}
	
	/** @return The value of <code>i</code> as octal string
	 * (e.g. <code>59</code> will return <code>"073"</code>). */
	public static String toOctalString(int i) {
		return "0" + Integer.toOctalString(i);
	}
	
	// ----------------------------------------------------------------------
	// from String
	// ----------------------------------------------------------------------
	
	/** Decodes <code>toDecode</code> as number or characters of a 
	 * type with a length (in bytes) of <code>len</code>
	 * and a endianess of <code>bigEndian</code>. 
	 * Parses hex (0xabcd), decimal and octal numbers.
	 * If used in a string (<code>len==-1</code>) 
	 * with the prefix <code>\</code> (<code>"Text\0x0fText"</code>) 
	 * the byte value is interpreted. 
	 * @return An array containing the bytes. */
	public static byte[] decode(int len, String toDecode, boolean bigEndian) {
		String number = toDecode;
		byte[] ret;

		if (len > 0)
			ret = new byte[len];
		else
			ret = new byte[toDecode.getBytes().length]; // maximal length

		byte pos = 0;

		while (!number.equals("") && pos < ret.length) {
			// number at start or normal char or escaped char?
			if ((number.startsWith("\\") 
					&& (number.charAt(1) >= '0' && number.charAt(1) <= '9')) 
					|| (number.startsWith("\\x") 
						|| number.startsWith("\\0x")
						|| (number.startsWith("0x")))) {
				byte[] bH;
				if (number.startsWith("\\0x") 
						|| number.startsWith("\\x") 
						|| number.startsWith("0x")) {
					// hexadecimal
					String n = number.substring(number.indexOf('x') + 1);
					int iEnd = 0;
					// Search end of hex number
					while (iEnd < n.length()
						&& ((n.charAt(iEnd) >= '0' && n.charAt(iEnd) <= '9')
							|| (n.charAt(iEnd) >= 'a'
								&& n.charAt(iEnd) <= 'f')
							|| (n.charAt(iEnd) >= 'A'
								&& n.charAt(iEnd) <= 'F')))
						iEnd++;
					// only pairs - ignore trailing single char
					iEnd = (iEnd >> 1) << 1;
					// cut number from input
					number = number.substring(number.indexOf('x') + 1 + iEnd);
					// get hex as byte[]
					bH = ByteHelper.hexStringToByte(n.substring(0, iEnd));
					Error.assertTrue((len < 0 
								|| (bH.length <= len && len > 0)),
						"Number does not fit into type! " 
							+ " size:" + len + " number: " + toDecode);

					// copy into ret, checking endianess
					// move to right, if not long enough
					if(bH.length<len) {
						byte[] bH2 = new byte[len];
						for(int iH = bH2.length - bH.length, iH2 = 0; iH < bH2.length && iH2 < bH.length; iH++, iH2++) {
							bH2[iH] = bH[iH2];
						}
						bH = bH2;
					}
					byte bStart, bEnd, bDir;
					if (!bigEndian) {
						bStart = (byte) (bH.length - 1);
						bEnd = -1;
						bDir = -1;
					} else {
						bStart = 0;
						bEnd = (byte) bH.length;
						bDir = 1;
					}
					byte bPos = bStart;
					// append to ret but use right direction in bH
					while (bPos != bEnd && pos < ret.length) {
						ret[pos] = bH[bPos];
						pos++;
						bPos += bDir;
					}
				} else {
					// octal
					String n = number.substring(1);
					int iEnd = 0;
					// search end of number
					while(iEnd < 3 && iEnd < n.length() 
								&& n.charAt(iEnd) <= '9' && n.charAt(iEnd) >= '0')
						iEnd++;
					// cut 
					n = n.substring(0, iEnd);
					ret[pos++] = (byte) Integer.parseInt(n, 8);
					// cut number with leading escape char
					number = number.substring(1 + iEnd);
				}
			} else { // if(number.startsWith("\\"))
				if(len==-1) { 
					// no number -- append char 
					ret[pos++] = (byte) number.charAt(0);
					number = number.substring(1);
				} else {
					// a number - convert to byte and append
					try {
						long l = Long.parseLong(number);
						byte[] bH = ByteHelper.hexStringToByte(Long.toHexString(l));
						// move to right, if not long enough
						if(bH.length<len) {
							byte[] bH2 = new byte[len];
							for(int iH = bH2.length - bH.length, iH2 = 0; iH < bH2.length && iH2 < bH.length; iH++, iH2++) {
								bH2[iH] = bH[iH2];
							}
							bH = bH2;
						}
						// copy into ret, checking endianess
						byte bStart, bEnd, bDir;
						if (!bigEndian) {
							bStart = (byte) (bH.length - 1);
							bEnd = 0 - 1;
							bDir = -1;
						} else {
							bStart = 0;
							bEnd = (byte) bH.length;
							bDir = 1;
						}
						byte bPos = bStart;
						while(bPos != bEnd && pos < ret.length) {
							ret[pos++] = bH[bPos];
							bPos += bDir;
						}
						Error.assertTrue(pos == ret.length, "Cannot determine value: " + toDecode);
					} catch (NumberFormatException e) {
						Error.errorExit("Cannot determine value: " + toDecode, e);
					}
				}
			}
		}
		// "cut" trailing space
		if(pos < ret.length && (ret.length != len || len < 0)) {
			byte[] bH = new byte[pos];
			System.arraycopy(ret, 0, bH, 0, pos);
			ret = bH;
		}
		return ret;
	}
	
	// ----------------------------------------------------------------------
	// Other methods
	// ----------------------------------------------------------------------
	
	/** Get the minimal and maximal value from the given buffer.
	 * @return The minimum in [0], the maximum in [1]. */
	public static byte[] getMinMax(byte[] buffer) {
		byte[] ret = new byte[2];
		ret[0] = Byte.MAX_VALUE;
		ret[1] = Byte.MIN_VALUE;
		for(int i = 0; i < buffer.length; i++) {
			if(buffer[i] < ret[0])
				ret[0] = buffer[i];
			if(buffer[i] > ret[1])
				ret[1] = buffer[i];
		}
		return ret;
	}
}
