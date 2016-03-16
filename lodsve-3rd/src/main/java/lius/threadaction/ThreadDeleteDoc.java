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



import lius.exception.LiusException;
import lius.lucene.LuceneActions;

import org.apache.lucene.index.Term;





import org.apache.log4j.Logger;







/**

 * Classe utilisant des Threads pour supprimer des documents de l'index.

 * <br/><br/>

 * Class using threads to delete documents from index.

 *

 * @author Rida Benjelloun (ridabenjelloun@gmail.com)

 */

public class ThreadDeleteDoc

        extends Thread {

  static Logger logger = Logger.getRootLogger();

  private String dir = "";

  private String field = "";

  private String content = "";

  private Term term = null;

  private boolean populated = false;

  private boolean populatedWithTerm = false;





  public ThreadDeleteDoc(String dir, String field, String content) {

    this.dir = dir;

    this.field = field;

    this.content = content;

    populated = true;



  }



  public ThreadDeleteDoc(String dir, Term term) {

    this.dir = dir;

    this.term = term;

    boolean populatedWithTerm = true;

  }



  public void run() {

    if (populated == true) {



      try {

        LuceneActions.getSingletonInstance().deleteDoc(dir, field, content);

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



    }

    else if (populatedWithTerm == true) {

      try {

        LuceneActions.getSingletonInstance().deleteDoc(dir, term);



      }

      catch (LiusException e) {

        logger.error(e.getMessage());

      }

    }



  }

}