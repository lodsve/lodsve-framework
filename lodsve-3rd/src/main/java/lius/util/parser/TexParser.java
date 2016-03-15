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

/**
 * Class: TexParser <br>
 * 
 * Read some interesting meta-data from LaTeX source files.
 * 
 * Changelog:
 * <ul>
 * <li>01.06.2005: Initial implementation. Does not take any tex-options into
 * account yet. Just a quick hack so far..</li>
 * </ul>
 * 
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */
public class TexParser {

	private static final String PATTERN_DOCUMENTCLASS_START = "\\documentclass{";
	private static final String PATTERN_DOCUMENTCLASS_END = "}";
	private static final String PATTERN_AUTHOR_START = "\\author{";
	private static final String PATTERN_AUTHOR_END = "}";
	private static final String PATTERN_TITLE_START = "\\title{";
	private static final String PATTERN_TITLE_END = "}";
	private static final String PATTERN_ABSTRACT_START = "\\begin{abstract}";
	private static final String PATTERN_ABSTRACT_END = "\\end{abstract}";
	private static final String PATTERN_CONTENT_START = "\\begin{document}";
	private static final String PATTERN_CONTENT_END = "\\end{document}";

	private InputStream stream;
	private String texDocumentclass = null;
	private String texAuthor = null;
	private String texTitle = null;
	private String texAbstract = null;
	private String texContent = null;

	public TexParser( InputStream texStream ) throws IOException {
		this.stream = texStream;
		parse();
	}

	public TexParser( String texFilename ) throws IOException {
		this( new FileInputStream( texFilename ) );
	}

	public TexParser( File texFile ) throws IOException {
		this( new FileInputStream( texFile ) );
	}

	private String getField( String text, String startPattern, String endPattern ) {
		int startIndex = text.indexOf( startPattern );
		int endIndex = text.indexOf( endPattern, startIndex );
		if ( ( startIndex != -1 ) && ( endIndex != -1 ) )
			return text
					.substring( startIndex + startPattern.length(), endIndex );
		else
			return null;
	}

	private void parse() throws IOException {
		if ( stream != null ) {
			BufferedReader br = new BufferedReader( new InputStreamReader(
					stream ) );
			String line = null;
			StringBuffer texBuffer = new StringBuffer();
			// read the whole tex document in a buffer
			while ( ( line = br.readLine() ) != null ) {
				texBuffer.append( line );
			}
			String texSource = texBuffer.toString();

			texTitle = getField( texSource, PATTERN_TITLE_START,
					PATTERN_TITLE_END );
			texAuthor = getField( texSource, PATTERN_AUTHOR_START,
					PATTERN_AUTHOR_END );
			texDocumentclass = getField( texSource,
					PATTERN_DOCUMENTCLASS_START, PATTERN_DOCUMENTCLASS_END );
			texAbstract = getField( texSource, PATTERN_ABSTRACT_START,
					PATTERN_ABSTRACT_END );
			texContent = getField( texSource, PATTERN_CONTENT_START,
					PATTERN_CONTENT_END );

		} else
			throw new IllegalStateException( "No InputStream available." );
	}

	public String getTitle() {
		return texTitle;
	}

	public String getAuthor() {
		return texAuthor;
	}

	public String getDocumentclass() {
		return texDocumentclass;
	}

	public String getAbstract() {
		return texAbstract;
	}

	public String getContent() {
		return texContent;
	}

	public static void main( String[] args ) {

		if ( args.length == 0 ) {
			System.out.println( "Usage: TexParser <tex file>" );
			System.exit( 1 );
		}
		try {
			TexParser tp = new TexParser( args[0] );
			System.out.println( "LaTeX Document Class : "
					+ tp.getDocumentclass() );
			System.out.println( "Author   : " + tp.getAuthor() );
			System.out.println( "Title    : " + tp.getTitle() );
			System.out.println( "Abstract : " + tp.getAbstract() );
			if ( tp.getContent() != null ) {
				System.out.println( "Content  : " );
				System.out.println( tp.getTitle() );
			}
		} catch ( IOException e ) {
			e.printStackTrace( System.err );
		}
	}

}