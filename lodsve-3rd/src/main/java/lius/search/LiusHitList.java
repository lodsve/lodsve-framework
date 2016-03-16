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
import java.io.Serializable;
import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lius.config.LiusConfig;
import lius.config.LiusField;
import lius.lucene.AnalyzerFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;

/**
 * @author Vincent Dussault (vincent.dussault@doculibre.com)
 */
public class LiusHitList extends AbstractList implements Serializable {

    private Hits luceneHits;

    private LiusConfig liusConfig;

    private Query luceneQuery;

    private Directory indexDirectory;

    private Searcher luceneSearcher;

    public LiusHitList(
            Hits luceneHits,
            Query luceneQuery,
            LiusConfig liusConfig,
            Directory directory,
            Searcher luceneSearcher) {
        this(luceneHits, luceneQuery, liusConfig, new Directory[] { directory }, luceneSearcher);
    }

    public LiusHitList(
            Hits luceneHits,
            Query luceneQuery,
            LiusConfig liusConfig,
            Directory[] directories,
            Searcher luceneSearcher) {
        super();
        this.luceneHits = luceneHits;
        this.liusConfig = liusConfig;
        this.luceneQuery = luceneQuery;
        this.indexDirectory = directories[0];
        this.luceneSearcher = luceneSearcher;
    }

    public Object get(int index) {
        try {
            return buildLiusHit(index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return luceneHits.length();
    }

    private LiusHit buildLiusHit(int index) throws IOException {

        LiusHit liusHit = new LiusHit();
        liusHit.setScore(luceneHits.score(index));
        liusHit.setDocId(luceneHits.id(index));

        Document luceneDocument = luceneHits.doc(index);

        Map liusHitFieldsMap = new HashMap();
        List liusFieldsList = new ArrayList();
        Highlighter luceneHighlighter = null;

        if (liusConfig.getHighlighter() == true) {
            IndexReader luceneIndexReader = IndexReader.open(indexDirectory);

            Query rewrittenLuceneQuery = luceneQuery.rewrite(luceneIndexReader);
            QueryScorer luceneScorer = new QueryScorer(rewrittenLuceneQuery);

            SimpleHTMLFormatter luceneFormatter = new SimpleHTMLFormatter(
                    "<span class=\"liusHit\">",
                    "</span>");
            luceneHighlighter = new Highlighter(luceneFormatter, luceneScorer);
        }

        for (int j = 0; j < liusConfig.getDisplayFields().size(); j++) {
            LiusField configLiusField = (LiusField) liusConfig.getDisplayFields().get(j);
            LiusField hitLiusField = new LiusField();
            String fieldName = configLiusField.getName();

            hitLiusField.setName(fieldName);
            hitLiusField.setLabel(configLiusField.getLabel());

            if (luceneHighlighter != null) {
                Fragmenter luceneFragmenter;
                if (configLiusField.getFragmenter() != null) {
                    luceneFragmenter = new SimpleFragmenter(Integer.parseInt(configLiusField.getFragmenter()));
                } else {
                    luceneFragmenter = new SimpleFragmenter(Integer.MAX_VALUE);
                }
                luceneHighlighter.setTextFragmenter(luceneFragmenter);
            }
            String[] luceneDocumentValues = luceneDocument.getValues(configLiusField.getName());
            if (luceneDocumentValues != null) {
                if (luceneHighlighter != null) {
                    for (int k = 0; k < luceneDocumentValues.length; k++) {
                        Analyzer luceneAnalyzer = AnalyzerFactory.getAnalyzer(liusConfig);
                        TokenStream luceneTokenStream = luceneAnalyzer.tokenStream(
                                configLiusField.getName(),
                                new StringReader(luceneDocumentValues[k]));
                        String fragment = null;
                        if (configLiusField.getFragmenter() != null)
                            fragment = luceneHighlighter.getBestFragments(
                                    luceneTokenStream,
                                    luceneDocumentValues[k],
                                    5,
                                    "...");
                        else {
                            fragment = luceneHighlighter.getBestFragment(
                                    luceneTokenStream,
                                    luceneDocumentValues[k]);
                        }

                        if (fragment == null) {
                        } else {
                            luceneDocumentValues[k] = fragment;
                        }
                    }
                }

                hitLiusField.setValue(luceneDocumentValues[0]);
                hitLiusField.setValues(luceneDocumentValues);


                liusHitFieldsMap.put(configLiusField.getName(), hitLiusField);
                liusFieldsList.add(hitLiusField);
            }

        }
        liusHit.setLiusFieldsMap(liusHitFieldsMap);
        liusHit.setLiusFields(liusFieldsList);
        return liusHit;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        luceneSearcher.close();
    }

}
