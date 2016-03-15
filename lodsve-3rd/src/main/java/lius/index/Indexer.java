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

package lius.index;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import lius.config.LiusConfig;
import lius.config.LiusField;
import lius.exception.LiusException;
import lius.lucene.LuceneActions;
import lius.transaction.LiusTransactionManager;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */
public abstract class Indexer {

	static Logger logger = Logger.getRootLogger();
	
	private LiusConfig lc = null;

	private Directory ramDir = null;

	private LiusTransactionManager transaction = null;

	private Document luceneDoc = null;

	private InputStream streamToIndex = null;

	private String mimeType = null;

	private String fileName = null;

	private Object objectToIndex = null;

	private Object mixedContentsObj = null;
	
	private String docToIndexPath = null;

	public final static int INDEXER_CONFIG_FIELDS_COL = 1;

	public final static int INDEXER_CONFIG_FIELDS_MAP = 2;

	public abstract int getType();

	public abstract boolean isConfigured();

	public abstract Collection getConfigurationFields();

	public abstract String getContent();

	public abstract Collection getPopulatedLiusFields();

	public void setUp(LiusConfig lc) {
		this.lc = lc;
	}

	public LiusConfig getLiusConfig() {
		return lc;
	}

	public synchronized void indexUsingTransaction(String indexDir) {
		luceneDoc = LuceneActions.getSingletonInstance().populateLuceneDoc(
				getPopulatedLiusFields());
		if(docToIndexPath != null)
			luceneDoc.add(new Field( "filePath",docToIndexPath, Field.Store.YES,Field.Index.UN_TOKENIZED));
		ramDir = new RAMDirectory();
		transaction = new LiusTransactionManager(luceneDoc, ramDir, indexDir,
				getLiusConfig());
		try {
			transaction.start();
			transaction.commit();
		} catch (LiusException e) {
			logger.error(e.getMessage());
			transaction.rollBack();
		} catch (IOException e) {
			logger.error(e.getMessage());
			transaction.rollBack();
		}
	}

	public synchronized void index(String indexDir) {
		luceneDoc = LuceneActions.getSingletonInstance().populateLuceneDoc(
				getPopulatedLiusFields());
		if(docToIndexPath != null)
			luceneDoc.add(new Field( "filePath",docToIndexPath, Field.Store.YES,Field.Index.UN_TOKENIZED));
		try {
			LuceneActions.getSingletonInstance().index(luceneDoc, indexDir, lc);
		} catch (LiusException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public synchronized void index(String indexDir, List LuceneCostumFields) {
		Document luceneDoc = LuceneActions.getSingletonInstance()
				.populateLuceneDoc(getPopulatedLiusFields());
		
		if(docToIndexPath != null)
			luceneDoc.add(new Field( "filePath",docToIndexPath, Field.Store.YES,Field.Index.UN_TOKENIZED));
		
		for (int i = 0; i < LuceneCostumFields.size(); i++) {
			luceneDoc.add((Field) LuceneCostumFields.get(i));
		}
		
		try {
			LuceneActions.getSingletonInstance().index(luceneDoc, indexDir, lc);
		} catch (LiusException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public synchronized void indexWithCostumLiusFields(String indexDir,
			List LuceneCostumFields) {
		Document luceneDoc = LuceneActions.getSingletonInstance()
				.populateLuceneDoc(getPopulatedLiusFields());
		
		if(docToIndexPath != null)
			luceneDoc.add(new Field( "filePath",docToIndexPath, Field.Store.YES,Field.Index.UN_TOKENIZED));
		
		for (int i = 0; i < LuceneCostumFields.size(); i++) {
			LiusField lf = (LiusField) LuceneCostumFields.get(i);

			Field field = null;
			if (lf.getType().equalsIgnoreCase("text")) {
				field = new Field(lf.getName(), lf.getValue(), Field.Store.YES, Field.Index.TOKENIZED);
			} else if (lf.getType().equalsIgnoreCase("keyword")) {
				field = new Field(lf.getName(), lf.getValue(),Field.Store.YES, Field.Index.UN_TOKENIZED);
			} else if (lf.getType().equalsIgnoreCase("unindexed")) {
				field = new Field(lf.getName(), lf.getValue(),Field.Store.YES, Field.Index.NO);
			} else if (lf.getType().equalsIgnoreCase("unstored")) {
				field = new Field(lf.getName(), lf.getValue(),Field.Store.NO, Field.Index.TOKENIZED);
			}
			logger
					.info("New field added to document : " + lf.getName()
							+ " (type = " + lf.getType() + ") " + " : "
							+ lf.getValue());
			if (field != null) {
				luceneDoc.add(field);
			}

		}
		try {
			LuceneActions.getSingletonInstance().index(luceneDoc, indexDir, lc);
		} catch (LiusException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	

	public synchronized void indexUsingTransaction(String indexDir,
			List LuceneCostumFields) {
		Document luceneDoc = LuceneActions.getSingletonInstance()
				.populateLuceneDoc(getPopulatedLiusFields());
		
		if(docToIndexPath != null)
			luceneDoc.add(new Field( "filePath",docToIndexPath, Field.Store.YES,Field.Index.UN_TOKENIZED));
		
		for (int i = 0; i < LuceneCostumFields.size(); i++) {
			luceneDoc.add((Field) LuceneCostumFields.get(i));
		}
		ramDir = new RAMDirectory();
		transaction = new LiusTransactionManager(luceneDoc, ramDir, indexDir,
				getLiusConfig());
		try {
			transaction.start();
			transaction.commit();
		} catch (LiusException e) {
			logger.error(e.getMessage());
			transaction.rollBack();
		} catch (IOException e) {
			logger.error(e.getMessage());
			transaction.rollBack();
		}
	}

	public InputStream getStreamToIndex() {
		return streamToIndex;
	}

	public void setStreamToIndex(InputStream streamToIndex) {
		this.streamToIndex = streamToIndex;
	}

	public Object getObjectToIndex() {
		return objectToIndex;
	}

	public void setObjectToIndex(Object objectToIndex) {
		this.objectToIndex = objectToIndex;
	}

	public Object getMixedContentsObj() {
		return mixedContentsObj;
	}

	public void setMixedContentsObj(Object mixedContentsObj) {
		this.mixedContentsObj = mixedContentsObj;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocToIndexPath() {
		return docToIndexPath;
	}

	public void setDocToIndexPath(String docToIndexPath) {
		this.docToIndexPath = docToIndexPath;
	}

}