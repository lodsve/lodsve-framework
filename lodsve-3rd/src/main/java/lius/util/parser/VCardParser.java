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

package lius.util.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import lius.util.VCard;


/**
 * Class: VCardParser <br>
 *
 * Changelog:
 * <ul>
 * <li>02.06.2005: Initial implementation (jf)</li>
 * <li>03.06.2005: Fixed card detection code (no reg-ex anymore) (jf)</li>
 * </ul>
 *
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */
public class VCardParser {

	private static final String lineSep = System.getProperty( "line.separator" );
	private static final String BEGIN_PATTERN = "BEGIN:VCARD";
	private static final String END_PATTERN = "END:VCARD";

	private InputStream stream = null;
	private List cards = new Vector();

	public VCardParser( InputStream stream ) throws IOException {
		// setup the input stream and parse it into the cards List
		this.stream = stream;
		parse( stream );
	}

	public VCardParser( File file ) throws IOException {
		this( new FileInputStream( file ) );
	}

	public VCardParser( String filename ) throws IOException {
		this( new File( filename ) );
	}

	/**
	 * parse
	 *
	 * @param stream2
	 * @throws IOException
	 */
	private void parse( InputStream stream ) throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader( stream ) );
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ( ( line = br.readLine() ) != null ) {
			if ( line.startsWith( BEGIN_PATTERN ) ) {
				StringBuffer cardBuffer = new StringBuffer();
				// new VCard begins
				cardBuffer.append( line ).append( lineSep );
				// finish reading of card content..
				String cardString = finishCard( br, cardBuffer );
				// add the new card to our container as a new VCard object
				cards.add( new VCard( cardString ) );
			}
		}
	}

	/**
	 * finishCard
	 *
	 * @param br
	 * @param cardBuffer
	 * @return @throws
	 *         IOException
	 */
	private String finishCard( BufferedReader br, StringBuffer cardBuffer )
			throws IOException {
		String line = null;
		// simply loop until EOF is reached. if a valid card end marker is found
		// earlier we'll
		// return directly from within the loop..
		while ( ( line = br.readLine() ) != null ) {
			// adjust for (invalid) short lines (e.g. containing only CR/LF)
			if ( line.length() > 2 )
				cardBuffer.append( line ).append( lineSep );

			if ( line.startsWith( END_PATTERN ) ) {
				return cardBuffer.toString().trim();
			}
		}
		// if a card is not correctly ending, fix it and return what we have so
		// far
		cardBuffer.append( END_PATTERN );
		return cardBuffer.toString();
	}

	public List getCards() {
		return cards;
	}

	public void setCards( List cards ) {
		this.cards = cards;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream( InputStream stream ) {
		this.stream = stream;
	}
}