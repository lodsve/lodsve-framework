/* * Licensed to the Apache Software Foundation (ASF) under one or more * contributor license agreements.  See the NOTICE file distributed with * this work for additional information regarding copyright ownership. * The ASF licenses this file to You under the Apache License, Version 2.0 * (the "License"); you may not use this file except in compliance with * the License.  You may obtain a copy of the License at * *      http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */package lius.threadaction;

import java.io.*;import lius.exception.LiusException;import lius.lucene.LuceneActions;
import org.apache.lucene.index.Term;



import org.apache.log4j.Logger;


/**
 * Classe utilisant des threads pour mettre à jour des documents dans l'index.
 * <br/><br/>
 * Class using threads for updating documents in index.
 *
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */
public class ThreadIndexUpdating
    extends Thread {
   static Logger logger = Logger.getRootLogger();

  private String dir = "";
  private String field = "";
  private String content = "";
  private String fileToIndex = "";
  private String config = "";
  private Term term = null;
  private boolean populated = false;
  private boolean populatedWithTerm = false;

  public ThreadIndexUpdating(String dir, String field, String content,
                             String fileToIndex, String config) {
    this.dir = dir;
    this.field = field;
    this.content = content;
    this.fileToIndex = fileToIndex;
    this.config = config;
    populated = true;
  }

  public ThreadIndexUpdating(String dir, Term term, String fileToIndex,
                             String config) {
    this.dir = dir;
    this.term = term;
    this.fileToIndex = fileToIndex;
    this.config = config;
    boolean populatedWithTerm = true;
  }

  public void run() {
    if (populated == true) {

      try {
        LuceneActions.getSingletonInstance().updateDoc(dir, field, content,
            fileToIndex, config);
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException ex) {
          logger.error(ex.getMessage());
        }

      }
      catch (LiusException e) {
        logger.error(e.getMessage());
      }
      catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    else if (populatedWithTerm == true) {

      try {
        LuceneActions.getSingletonInstance().updateDoc(dir, term, fileToIndex,
            config);
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException ex) {
          logger.error(ex.getMessage());
        }

      }
      catch (LiusException e) {
        logger.error(e.getMessage());
      }
      catch (IOException e) {
        logger.error(e.getMessage());
      }

    }

  }

}