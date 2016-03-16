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


package lius.index.audio;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.sound.sampled.UnsupportedAudioFileException;

import lius.config.LiusField;
import lius.index.Indexer;

import org.apache.log4j.Logger;



/**

 * Class: MP3Indexer <br>

 *

 * Changelog:

 * <ul>

 * <li>17.05.2005: Initial implementation</li>

 * </ul>

 *

 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>

 */

public class MP3Indexer extends Indexer {



	static Logger logger = Logger.getRootLogger();


	public int getType() {
		return 1;
	}



	public boolean isConfigured() {
		boolean ef = false;
		if(getLiusConfig().getMP3Fields() != null)
			return ef = true;
		return ef;
	}


	public Collection getConfigurationFields() {
		return getLiusConfig().getMP3Fields();
	}



	public Collection getPopulatedLiusFields() {

		Collection c = new ArrayList();

		MpegInfo mi = new MpegInfo();

		try {

			mi.load(getStreamToIndex() );

			for ( Iterator i = getLiusConfig().getMP3Fields().iterator(); i.hasNext(); ) {

				Object next = i.next();

				if ( next instanceof LiusField ) {

					LiusField lf = (LiusField) next;

					if ( "channels".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getChannels() );

						c.add( lf );

					} else if ( "channelsmode".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( mi.getChannelsMode() );

						c.add( lf );

					} else if ( "version".equalsIgnoreCase( lf.getGet() )

							&& mi.getVersion() != null ) {

						lf.setValue( mi.getVersion() );

						c.add( lf );

					} else if ( "samplingrate".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getSamplingRate() );

						c.add( lf );

					} else if ( "layer".equalsIgnoreCase( lf.getGet() )

							&& mi.getLayer() != null ) {

						lf.setValue( mi.getLayer() );

						c.add( lf );

					} else if ( "emphasis".equalsIgnoreCase( lf.getGet() )

							&& mi.getEmphasis() != null ) {

						lf.setValue( mi.getEmphasis() );

						c.add( lf );

					} else if ( "nominalbitrate".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getBitRate() );

						c.add( lf );

					} else if ( "duration".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getPlayTime() );

						c.add( lf );

					} else if ( "location".equalsIgnoreCase( lf.getGet() )

							&& mi.getLocation() != null ) {

						lf.setValue( mi.getLocation() );

						c.add( lf );

					} else if ( "size".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getSize() );

						c.add( lf );

					} else if ( "copyright".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( mi.getCopyright() ? "true" : "false" );

						c.add( lf );

					} else if ( "crc".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( mi.getCRC() ? "true" : "false" );

						c.add( lf );

					} else if ( "original".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( mi.getOriginal() ? "true" : "false" );

						c.add( lf );

					} else if ( "vbr".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( mi.getVBR() ? "true" : "false" );

						c.add( lf );

					} else if ( "track".equalsIgnoreCase( lf.getGet() ) ) {

						lf.setValue( "" + mi.getTrack() );

						c.add( lf );

					} else if ( "year".equalsIgnoreCase( lf.getGet() )

							&& mi.getYear() != null ) {

						lf.setValue( mi.getYear() );

						c.add( lf );

					} else if ( "genre".equalsIgnoreCase( lf.getGet() )

							&& mi.getGenre() != null ) {

						lf.setValue( mi.getGenre() );

						c.add( lf );

					} else if ( "title".equalsIgnoreCase( lf.getGet() )

							&& mi.getTitle() != null ) {

						lf.setValue( mi.getTitle() );

						c.add( lf );

					} else if ( "artist".equalsIgnoreCase( lf.getGet() )

							&& mi.getArtist() != null ) {

						lf.setValue( mi.getArtist() );

						c.add( lf );

					} else if ( "album".equalsIgnoreCase( lf.getGet() )

							&& mi.getAlbum() != null ) {

						lf.setValue( mi.getAlbum() );

						c.add( lf );

					} else if ( "comments".equalsIgnoreCase( lf.getGet() )

							&& mi.getComment() != null ) {



						lf.setValue( mi.getComment().toString() );

						c.add( lf );



					}

				} else

					c.add( next );

			}

		} catch ( IOException e ) {

			logger.error( e.getMessage() );



		} catch ( UnsupportedAudioFileException e ) {

			logger.error( e.getMessage() );

		}



		return c;

	}


	public String getContent() {

		return null;
	}





}