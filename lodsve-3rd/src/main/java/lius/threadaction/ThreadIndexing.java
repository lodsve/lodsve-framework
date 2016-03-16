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

package lius.threadaction;



import java.io.*;

import lius.exception.LiusException;
import lius.lucene.LuceneActions;





import org.apache.log4j.Logger;







/**

 * Classe utilisant des Threads pour indexer des documents.

 * <br/><br/>

 * Class using threads for indexing documents.

 *

 * @author Rida Benjelloun (ridabenjelloun@gmail.com)

 */

public class ThreadIndexing

        extends Thread {

  static Logger logger = Logger.getRootLogger();

  private String toIndex = null;

  private String indexDir = "";

  private String fichierXMLConfig = "";



  public ThreadIndexing(String toIndex,

                        String indexDir,

                        String fichierXMLConfig) {

    this.toIndex = toIndex;

    this.indexDir = indexDir;

    this.fichierXMLConfig = fichierXMLConfig;

  }

  public ThreadIndexing(String toIndex,

                        String indexDir,

                        String fichierXMLConfig,

                        String type) {

    this.toIndex = toIndex;

    this.indexDir = indexDir;

    this.fichierXMLConfig = fichierXMLConfig;

  }





  public void run() {



    try {

      LuceneActions.getSingletonInstance().index(toIndex, indexDir,

              fichierXMLConfig);

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