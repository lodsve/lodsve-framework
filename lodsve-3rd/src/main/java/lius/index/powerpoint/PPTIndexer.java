package lius.index.powerpoint;

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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lius.config.LiusField;
import lius.index.Indexer;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;


/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */

public class PPTIndexer extends Indexer implements POIFSReaderListener {

	private ByteArrayOutputStream writer;

	static Logger logger = Logger.getRootLogger();

	public int getType() {
		return 1;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if (getLiusConfig().getPPTFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getPPTFields();
	}

	public String getContent() {
		String contents = "";
		try {
			POIFSReader reader = new POIFSReader();
			writer = new ByteArrayOutputStream();
			reader.registerListener(this);
			reader.read(getStreamToIndex());
			contents = writer.toString();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return contents;
	}

	public Collection getPopulatedLiusFields() {
		Collection coll = new ArrayList();
		Iterator it = getLiusConfig().getPPTFields().iterator();
		while (it.hasNext()) {
			Object field = it.next();
			if (field instanceof LiusField) {
				LiusField lf = (LiusField) field;
				if (lf.getGet() != null) {
					if (lf.getGet().equalsIgnoreCase("content")) {
						String content = getContent();
						lf.setValue(content);
						coll.add(lf);
					}
				}
			} else {
				coll.add(field);
			}
		}
		return coll;
	}

	public void processPOIFSReaderEvent(POIFSReaderEvent event) {
		try {
			if (!event.getName().equalsIgnoreCase("PowerPoint Document"))
				return;
			DocumentInputStream input = event.getStream();
			byte[] buffer = new byte[input.available()];
			input.read(buffer, 0, input.available());
			for (int i = 0; i < buffer.length - 20; i++) {
				long type = LittleEndian.getUShort(buffer, i + 2);
				long size = LittleEndian.getUInt(buffer, i + 4);
				if (type == 4008L) {
					writer.write(buffer, i + 4 + 1, (int) size + 3);
					i = i + 4 + 1 + (int) size - 1;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

}