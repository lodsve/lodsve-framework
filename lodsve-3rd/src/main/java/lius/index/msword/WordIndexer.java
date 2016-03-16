package lius.index.msword;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lius.config.LiusField;
import lius.index.Indexer;

import org.apache.log4j.Logger;
import org.textmining.text.extraction.WordExtractor;


/**
 * Classe permettant d'indexer des fichiers Microsoft Word 
 * <br/><br/> 
 * Class for indexing Microsoft Word documents. 
 * @author Rida Benjelloun (ridabenjelloun@gmail.com) 
 */

public class WordIndexer extends Indexer {

	static Logger logger = Logger.getRootLogger();

	public int getType() {
		return 1;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if (getLiusConfig().getMsWordFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getMsWordFields();
	}

	public String getContent() {
		WordExtractor we = new WordExtractor();
		String text = "";
		try {
			text = we.extractText(getStreamToIndex());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return text;
	}

	/**
	 * Méthode retournant un objet de type Lucene document à partir du fichier à
	 * indexer et d'une collection d'objets de type LiusField. Chaque objet
	 * LiusField contient de l'information sur le nom du champs Lucene, le type,
	 * etc. <br/><br/> Method that return a Lucene object from the
	 * configuration file and a collection of LiusField objects. Each LiusField
	 * object contains information about the Lucene field, the type, etc.
	 *
	 */

	public Collection getPopulatedLiusFields() {
		Collection coll = new ArrayList();
		Iterator it = getLiusConfig().getMsWordFields().iterator();
		while (it.hasNext()) {
			Object field = it.next();
			if (field instanceof LiusField) {
				LiusField lf = (LiusField) field;
				if (lf.getGet() != null) {
					if (lf.getGet().equalsIgnoreCase("content")) {
						String text = getContent();
						lf.setValue(text);
						coll.add(lf);
					}
				}
			} else {
				coll.add(field);
			}
		}
		return coll;
	}

}
