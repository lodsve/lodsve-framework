package lius.index.javaobject;

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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lius.config.LiusField;
import lius.index.Indexer;

import org.apache.log4j.Logger;



/**
 * Classe permettant d'indexer des objets JavaBeans
 * <br/><br/>
 * Class for indexing JavaBeans objects.
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 *
 */

public class BeanIndexer  extends Indexer {

    static Logger logger = Logger.getRootLogger();

    public int getType() {
        return 2;
    }

    public boolean isConfigured() {
        boolean ef = false;
        if(getLiusConfig().getJavaBeansFields() != null)
            return ef = true;
        return ef;
    }


    public Collection getConfigurationFields() {
        List ls = new ArrayList();
        ls.add(getLiusConfig().getJavaBeansFields());
        return ls;
    }



    public Collection getPopulatedLiusFields() {
        Collection collRes = new ArrayList();
        try {
            Class c = getObjectToIndex().getClass();
            Collection liusFields = (Collection) getLiusConfig().getJavaBeansFields().get(c.getName());
            Iterator i = liusFields.iterator();

            while (i.hasNext()) {
                Object field = i.next();
                if (field instanceof LiusField) {
                    LiusField lf = (LiusField) field;
                    Method m = c.getMethod(lf.getGetMethod(), null);
                    Object o = m.invoke(getObjectToIndex(), null);
                    String value = o.toString();
                    lf.setValue(value);
                    collRes.add(lf);
                }
                else{
                    collRes.add(field);
                }
            }
        }
        catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return collRes;
    }


    public String getContent() {
        StringBuffer sb = new StringBuffer();
        try {
            Class c = getObjectToIndex().getClass();
            Collection liusFields = (Collection) getLiusConfig().getJavaBeansFields().get(c.getName());
            Iterator i = liusFields.iterator();

            while (i.hasNext()) {
                Object field = i.next();
                if (field instanceof LiusField) {
                    LiusField lf = (LiusField) field;
                    Method m = c.getMethod(lf.getGetMethod(), null);
                    Object o = m.invoke(getObjectToIndex(), null);
                    sb.append(o.toString()+" ");
                }
            }
        }
        catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return sb.toString();
    }


}