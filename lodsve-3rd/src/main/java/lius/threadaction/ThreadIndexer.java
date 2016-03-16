package lius.threadaction;

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

import java.io.File;
import java.io.IOException;

import lius.LiusLogger;
import lius.config.LiusConfig;
import lius.config.LiusConfigBuilder;
import lius.index.Indexer;
import lius.index.mixedindexing.MixedIndexer;
import lius.lucene.LuceneActions;

import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */


public class ThreadIndexer {

	static Logger logger = Logger.getRootLogger();

	private int nbThreadsN = 0;

	private static LiusConfig lc = null;

	private static String liusConfig = "";

	private File toIndexF = null;

	private String[] tmpsIndexPath;

	private int ct = 0;

	private String indexDir;

	public static int r = 0;

	private ThreadIndexer() {
	}

	public ThreadIndexer(String liusConfig, String indexDir, int nbThreads) {
		this.liusConfig = liusConfig;
		this.indexDir = indexDir;
		this.nbThreadsN = nbThreads - 1;
		if (nbThreads > 1) {
			tmpsIndexPath = new String[nbThreads];
			for (int i = 0; i < nbThreads; i++) {
				String tmpIndex = indexDir + File.separator + i;
				tmpsIndexPath[i] = tmpIndex;
			}
		} else {

			nbThreads = 2;
			tmpsIndexPath = new String[nbThreads];
			for (int i = 0; i < nbThreads; i++) {
				String tmpIndex = indexDir + File.separator + i;
				tmpsIndexPath[i] = tmpIndex;
			}
		}
		ct = nbThreadsN;
		lc = LiusConfigBuilder.getSingletonInstance().getLiusConfig(liusConfig);
	}

	public void setLogger(String loggerConfig) {
		LiusLogger.setLoggerConfigFile(loggerConfig);
	}

	public void index(String toIndex) {
		//startIndexing(toIndex);
		mergeIndexes(indexDir);
	}

	private synchronized void startIndexing(String toIndex) {

		File toIndexFile = new File(toIndex);
		File[] files = toIndexFile.listFiles();
		for(int i=0; i<files.length;i++ ){
			if(files[i].isDirectory()){
				if((files[i].getAbsolutePath()).length() == 26){
					System.out.println("OK");

					if(ct == 0){
						ct = nbThreadsN;
					}
					toIndexF = files[i];
					System.out.println(toIndexF);
					Thread t = new Thread(){
						public void run(){
							System.out.println(tmpsIndexPath[ct]);
							Indexer indexer = new MixedIndexer();
							indexer.setUp(lc);
							indexer.setMixedContentsObj(toIndexF);

							// IndexerFactory.getIndexer(toIndexF,lc);
							indexer.index(tmpsIndexPath[ct]);
							r++;
						}
					};
					t.start();
					try {
						t.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ct --;
				}
				startIndexing(files[i].getAbsolutePath());
			}


		}
	}	public void mergeIndexes(String indexDir) {
		File indexDirFile = new File(indexDir);
		File[] indexList = indexDirFile.listFiles();
		LuceneActions.getSingletonInstance().addIndexes(
				getDirectories(indexList), indexDir, lc);
		cleanIndex(indexDir);

	}

	private Directory[] getDirectories(File[] indexs) {
		Directory[] indexDirs = new Directory[indexs.length];
		for (int i = 0; i < indexs.length; i++) {
			try {
				System.out.println(indexs[i]);
				indexDirs[i] = FSDirectory.getDirectory(indexs[i].getAbsolutePath(), false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return indexDirs;
	}

	public void cleanIndex(String indexDir) {
		File indexDirFile = new File(indexDir);
		File[] indexList = indexDirFile.listFiles();
		for (int i = 0; i < indexList.length; i++) {
			if (indexList[i].isDirectory()) {
				indexList[i].deleteOnExit();
			}
		}
	}
}
