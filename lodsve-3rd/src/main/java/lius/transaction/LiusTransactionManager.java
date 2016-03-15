package lius.transaction;

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

import java.io.IOException;

import lius.config.LiusConfig;
import lius.exception.LiusException;
import lius.lucene.LuceneActions;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;

/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 * */
public class LiusTransactionManager {
	private Directory[] luceneTmpIndexTab = new Directory[1];
	private LiusConfig  conf = null; 
	private Document doc = null;
	private String permanentIndexPath = null;
	
	public LiusTransactionManager(Document doc, Directory luceneTmpIndex, String permanentIndexPath ,LiusConfig conf){
		this.luceneTmpIndexTab[0] = luceneTmpIndex;
		this.doc = doc;
		this.conf = conf;
		this.permanentIndexPath =permanentIndexPath; 
	}
	
	public void start() throws LiusException, IOException{		
			LuceneActions.getSingletonInstance().index(doc,this.luceneTmpIndexTab[0],conf);	
	}
	public void commit(){		
		LuceneActions.getSingletonInstance().addIndexes(luceneTmpIndexTab,permanentIndexPath,conf);
	}
	
	public void rollBack(){		
		luceneTmpIndexTab[0] = null;
	}

}
