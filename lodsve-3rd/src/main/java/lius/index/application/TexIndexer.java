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

package lius.index.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lius.config.LiusField;
import lius.index.Indexer;
import lius.util.parser.TexParser;

import org.apache.log4j.Logger;


/**
 * Class: TexIndexer <br>
 *
 * Changelog:
 * <ul>
 * <li>01.06.2005: Initial implementation</li>
 * </ul>
 *
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */

/**
 * Adapted by Rida Benjelloun 
 */
public class TexIndexer extends Indexer {
	static Logger logger = Logger.getRootLogger();

	public int getType() {
		return 1;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if(getLiusConfig().getTexFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getTexFields();
	}




	/**
	 * @see lius.index.Indexer#getPopulatedCollection(java.lang.Object,
	 *      java.util.Collection)
	 */
	public Collection getPopulatedLiusFields() {
		Collection c = new ArrayList();
		TexParser tp;
		try {
			tp = new TexParser(getStreamToIndex());
			for ( Iterator i = getLiusConfig().getTexFields().iterator(); i.hasNext(); ) {
				Object next = i.next();
				if ( next instanceof LiusField ) {
					LiusField lf = (LiusField) next;
					if ( "documentclass".equalsIgnoreCase( lf.getGet() ) ) {
						lf.setValue( "" + tp.getDocumentclass() );
						c.add( lf );
					} else if ( "title".equalsIgnoreCase( lf.getGet() ) ) {
						lf.setValue( tp.getTitle() );
						c.add( lf );
					} else if ( "author".equalsIgnoreCase( lf.getGet() ) ) {
						lf.setValue( tp.getAuthor() );
						c.add( lf );
					} else if ( "content".equalsIgnoreCase( lf.getGet() ) ) {
						lf.setValue( tp.getContent() );
						c.add( lf );
					} else if ( "abstract".equalsIgnoreCase( lf.getGet() ) ) {
						lf.setValue( tp.getAbstract() );
						c.add( lf );
					}
				} else
					c.add( next );
			}
		} catch ( IOException e ) {
			logger.error(e.getMessage());
		}

		return c;
	}


	public String getContent() {
		StringBuffer sb = new StringBuffer();
		TexParser tp = null;;

		try {
			tp = new TexParser(getStreamToIndex());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		sb.append(tp.getDocumentclass()+" ");
		sb.append(tp.getTitle()+" ");
		sb.append(tp.getAuthor()+" ");
		sb.append(tp.getContent()+" " );
		sb.append(tp.getAbstract()+" ");



		return sb.toString();
	}



}