package lius.index.mixedindexing;

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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lius.config.LiusField;
import lius.index.Indexer;
import lius.index.IndexerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;


/**
 *
 * Classe permettant d'effectuer une indexation mixte. Cette
 *
 * indexation permet d'integrer dans le même "Lucene Document"
 *
 * des méta-données dans format XML et le texte integral dans
 *
 * un fichier PDF Word etc.
 *
 * <br/><br/>
 *
 * Class for mixed indexation. This indexation allows for integrating in the
 *
 * same Lucene document XML metadata and full text from a PDF file, Word file,
 * etc.
 *
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 *
 */

public class MixedIndexer extends Indexer {

	static Logger logger = Logger.getRootLogger();

	public int getType() {
		return 0;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if (getLiusConfig().getMixedFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getMixedFields();
	}

	/**
	 * Retourne une collection contenant les champs avec les valeurs à indexer
	 * comme par exemple: le texte integrale, titre etc. <br/><br/>Returns a
	 * collection containing the fieds with the values to index, like : full
	 * text, title, etc.
	 */

	public Collection getPopulatedLiusFields() {
		List populatedList = new ArrayList();
		List indexers = IndexerFactory.getIndexers(getMixedContentsObj(),
				getLiusConfig());
		for (int i = 0; i < indexers.size(); i++) {
			Indexer indexer = (Indexer) indexers.get(i);
			if (indexer != null) {
				if ((indexer.isConfigured())
						&& (indexer.getType() == Indexer.INDEXER_CONFIG_FIELDS_COL)) {
					Iterator cit = getConfigurationFields().iterator();
					while (cit.hasNext()) {
						LiusField mf = (LiusField) cit.next();
						if (indexer.getMimeType().equals(mf.getFileMimeType())) {
							Collection populCollFile = indexer
									.getPopulatedLiusFields();
							if(indexer.getDocToIndexPath() != null){
								LiusField lff = new LiusField();
								lff.setName("path");
								String path = indexer.getDocToIndexPath();
								lff.setValue(path);
								lff.setType("Keyword");
								populCollFile.add(lff);
							}

							Iterator it = populCollFile.iterator();
							LiusField f = null;
							LiusField newLF = null;
							while (it.hasNext()) {
								Object o = it.next();
								try {
									if (o instanceof LiusField) {
										f = (LiusField) o;
										newLF = new LiusField();
										BeanUtils.copyProperties(newLF, f);
										populatedList.add(newLF);
									}
								} catch (InvocationTargetException ex) {
									logger.error(ex.getMessage());
								} catch (IllegalAccessException ex) {
									logger.error(ex.getMessage());
								}
							}
						}
					}
				}
			}
		}
		return populatedList;

	} // complete pour le type 2 cad HashMap

	public String getContent() {
		StringBuffer content = new StringBuffer();
		List indexers = IndexerFactory.getIndexers(getMixedContentsObj(),
				getLiusConfig());
		for (int i = 0; i < indexers.size(); i++) {
			Indexer indexer = (Indexer) indexers.get(i);
			if (indexer != null) {
				String nContent = indexer.getContent();
				if (nContent != null) {
					content.append(nContent);
				}
			}
		}
		return content.toString();
	}

}
