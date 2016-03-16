package lius.search;


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
import lius.config.LiusConfigBuilder;
import lius.lucene.LuceneActions;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.store.Directory;


/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 * @author Vincent Dussault (vincent.dussault@doculibre.com)
 */


public class SearchIndex {

    private static LiusConfig liusConfig;

    public static LiusHitList search(String luceneQueryString, String indexDir, String liusConfigPath) {
        try {

            liusConfig =  getLiusConfig(liusConfigPath);
            Directory indexDirectory = LuceneActions.getSingletonInstance().getDirectory(indexDir);
            IndexSearcher luceneSearcher = new IndexSearcher(indexDirectory);

            LiusQuery liusQuery = new LiusQuery();
            Query luceneQuery = liusQuery.getQueryFactory(liusConfig.getElemSearch(), liusConfig, luceneQueryString);

            Hits luceneHits = luceneSearcher.search(luceneQuery);

            return new LiusHitList(luceneHits, luceneQuery, liusConfig, indexDirectory, luceneSearcher);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LiusHitList multiIndexSearch(String luceneQueryString, String[] indexDirs, String liusConfigPath) {
        try {
            liusConfig =  getLiusConfig(liusConfigPath);
            Directory[] indexDirectories = LuceneActions.getSingletonInstance().getDirectories(indexDirs);
            MultiSearcher luceneSearcher = new MultiSearcher(buildSearchableTab(indexDirectories));

            LiusQuery liusQuery = new LiusQuery();
            Query luceneQuery = liusQuery.getQueryFactory(liusConfig.getElemSearch(), liusConfig, luceneQueryString);

            Hits luceneHits = luceneSearcher.search(luceneQuery);

            return new LiusHitList(luceneHits, luceneQuery, liusConfig, indexDirectories, luceneSearcher);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Searchable[] buildSearchableTab(Directory[] indexDirectories) {
        Searchable[] searchables = new Searchable[indexDirectories.length];
        try {
            for (int i = 0; i < indexDirectories.length; i++) {
                Directory indexDirectory = indexDirectories[i];
                searchables[i] = new IndexSearcher(indexDirectory);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return searchables;

    }

    private static LiusConfig getLiusConfig(String liusConfigPath){
        return LiusConfigBuilder.getSingletonInstance().getLiusConfig(liusConfigPath);
    }





}
