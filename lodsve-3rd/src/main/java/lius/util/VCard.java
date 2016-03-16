/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lius.util;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class: VCard <br>
 *
 * This class represents a single VCard which is either constructed or read from
 * a string that must begin with 'BEGIN:VCARD' and end with 'END:VCARD'. All
 * fields - including possible extensions (e.g. from mozilla) - will be read.
 * This class overrides the toString() method for outputting a constructed the
 * VCard entry.
 *
 * Changelog:
 * <ul>
 * <li>02.06.2005: Initial implementation (jf)</li>
 * <li>03.06.2005: changes from java.lang.String.contains() to indexOf() for
 * JDK 1.4 compatibility (jf)</li>
 * </ul>
 *
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */
public class VCard implements Serializable {

	public static String PATTERN_BEGIN = "BEGIN:VCARD";
	public static String PATTERN_END = "END:VCARD";
	private static String lineSep = System.getProperty( "line.separator" );
	private List cardElements = new Vector();

	/**
	 * container for a single VCard element/line (such as
	 * 'EMAIL;INTERNET:me@home.org')
	 */
	class VCElement {

		/**
		 * Construct a single VCard element
		 *
		 * @param key:
		 *           the element's key
		 * @param type:
		 *           the element's type (or null if n/a)
		 * @param data:
		 *           user data
		 */
		public VCElement( String key, String type, String data ) {
			this.key = key;
			this.type = type;
			this.data = data;
		}

		/**
		 * <code>key</code>: the element's key (e.g. 'EMAIL')
		 */
		String key;

		/**
		 * <code>type</code>: the element's type (might be null) (e.g.
		 * 'WORK')
		 */
		String type;

		/**
		 * <code>data</code>: the element's data (e.g. 'me@home.org')
		 */
		String data;

		public String toString() {
			return "[VCElement key=" + key + ", type=" + type + ", data="
					+ data + "]";
		}
	}

	public VCard() {
	}

	public VCard( String cardString ) {
		// check if we have a valid VCard string
		if ( cardString.startsWith( PATTERN_BEGIN )
				&& cardString.endsWith( PATTERN_END ) ) {

			// seperate the lines
			StringTokenizer lt = new StringTokenizer( cardString, lineSep );
			while ( lt.hasMoreTokens() ) {
				String line = lt.nextToken();

				// do we have a key/value-pair?
				int metaSep = line.indexOf( ':' );
				if ( ( metaSep != -1 )
						&& ( line.indexOf( PATTERN_BEGIN ) == -1 )
						&& ( line.indexOf( PATTERN_END ) == -1 ) ) {
					// seperate metadata from content
					String meta = line.substring( 0, metaSep );
					String content = line
							.substring( metaSep + 1, line.length() );

					// check to see if we can add this VCard element..
					if ( keyAllowed( meta ) ) {
						// do we have a subclassed field?
						int i = meta.indexOf( ';' );
						VCElement e = null;
						if ( i != -1 ) {
							// subclassed field found
							e = new VCElement( meta.substring( 0, i ), meta
									.substring( i, meta.length() ), content );

						} else {
							// simple field found
							e = new VCElement( meta, null, content );

						}
						// add the new element to the list of available elements
						cardElements.add( e );
					}
				}
			}
		} else
			throw new IllegalArgumentException(
					"Unsupported VCard format. Must " + PATTERN_BEGIN + " ... "
							+ PATTERN_END + "'" );
	}

	/**
	 * keyAllowed this method should be used to force exclusion of certain
	 * fields (e.g. binary content)
	 *
	 * @param meta
	 *           the meta (key/type) part of the element in question
	 * @return true, if this field should be added as a VCElement, otherwise
	 *         false
	 */
	private boolean keyAllowed( String meta ) {
		return !meta.startsWith( "PHOTO" );
	}

	/**
	 * getCardElements get all available elements
	 *
	 * @return List of VCard.VCElement
	 */
	public List getCardElements() {
		return cardElements;
	}

	/**
	 * setCardElements set the card elements
	 *
	 * @param cardElements:
	 *           List of VCard.VCElement
	 */
	public void setCardElements( List cardElements ) {
		this.cardElements = cardElements;
	}

	/**
	 * getElement iterates over all available card elements and returns the data
	 * parts for all matching key elements <br/>(note: use
	 * getElement(String,String) for additional type lookup)
	 *
	 * @param key:
	 *           the key to search for (e.g. 'EMAIL')
	 * @return the first matching element or null if no match was found
	 */
	public String getElement( String key ) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < cardElements.size(); i++ ) {
			VCElement e = (VCElement) cardElements.get( i );
			if ( key.equals( e.key ) )
				sb.append( e.data ).append( ", " );
		}
		String ret = sb.toString();
		// remove trailing ", "
		if ( ret.length() > 2 )
			return ret.substring( 0, ret.length() - 2 );
		else
			return null;
	}

	/**
	 * getElement iterates over all available card elements and returns the data
	 * parts for all matching key elements <br/>(note: use
	 * getElement(String,String) for additional type lookup)
	 *
	 * @param key:
	 *           the key to search for (e.g. 'EMAIL')
	 * @param type:
	 *           the type of the key to search for (e.g. 'WORK')
	 * @return the first matching element or null if no match was found
	 */
	public String getElement( String key, String type ) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < cardElements.size(); i++ ) {
			VCElement e = (VCElement) cardElements.get( i );
			if ( key.equals( e.key ) && type.equals( e.type ) )
				sb.append( e.data ).append( ", " );
		}
		String ret = sb.toString();
		// remove trailing ", "
		if ( ret.length() > 2 )
			return ret.substring( 0, ret.length() - 2 );
		else
			return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer( PATTERN_BEGIN ).append( lineSep );
		for ( int i = 0; i < cardElements.size(); i++ ) {
			VCElement e = (VCElement) cardElements.get( i );
			sb.append( e.key );
			if ( e.type != null )
				sb.append( ';' ).append( e.type );
			sb.append( ':' ).append( e.data ).append( lineSep );
		}
		sb.append( PATTERN_END );
		return sb.toString();
	}

}