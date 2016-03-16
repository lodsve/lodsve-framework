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

import java.lang.reflect.Constructor;
import lius.config.LiusConfig;
import lius.lucene.AnalyzerFactory;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;


/**
 *
 * Classe permettant de créer des objets de type Query.
 *
 * <br/><br/>
 *
 * Class that creates Query objects.
 *
 * @author Rida Benjelloun (ridabenjelloun@gmail)
 *
 */

public class LiusQuery {

	private Query q = null;

	static Logger logger = Logger.getRootLogger();

	/**
	 *
	 * Méthode utilisant la réflexion et permettant de créer des objets de type
	 * Query,
	 *
	 * dont le constructeur prend un objet de type Term.
	 *
	 * L'objet Term est construit à partir du nom du champs spécifié dans le
	 *
	 * fichier de configuration et de la valeur exprimée sous forme de paramètre
	 *
	 * "searchExp"de la méthode createQueryTerm.
	 *
	 * <br/><br/>
	 *
	 * The method uses reflection which creates object of type Query which
	 * constructor takes
	 *
	 * an object of type Term as parameter. The Term object is constructed from
	 *
	 * the name of the specified field in the configuration file and the value
	 * of
	 *
	 * "searchExp" parameter.
	 *
	 */

	public Query createQueryTerm(LiusConfig xc, String searchExp) {

		try {

			Class classe = Class.forName(xc.getQueryTermClass());

			Constructor[] con = classe.getConstructors();

			for (int i = 0; i < con.length; i++) {

				Class[] conParam = con[i].getParameterTypes();

				for (int j = 0; j < conParam.length; j++)

					if (conParam[j].getName().equals(
							"org.apache.lucene.index.Term")) {

						// Création de l'objet Term

						Term t = new Term(xc.getTermFiled(), searchExp);

						q = (Query) con[i].newInstance(new Object[] { t });

					}

			}

		}

		catch (ClassNotFoundException e) {

			logger.error("La classe n'a pas été trouvée" + e.getMessage());

		}

		catch (InstantiationException e) {

			logger.error("La classe est soit abstraite, soit une interface" +

					e.getMessage());

		}

		catch (IllegalAccessException e) {

			logger.error("La classe n'est pas accessible" + e.getMessage());

		}

		catch (java.lang.reflect.InvocationTargetException e) {

			logger.error(" Exception déclenchée si le constructeur invoqué" +

					e.getMessage());

		}

		catch (IllegalArgumentException e) {

			logger.error(" Mauvais type de paramètres " + e.getMessage());

		}

		return q;

	}

	/**
	 *
	 * Création d'un objet Query de type MultiFieldQueryParser à partir du
	 *
	 * fichier de configuration.
	 *
	 * <br/><br/<
	 *
	 * Creation of an object Query of type MultiFieldQueryParser from the
	 *
	 * configuration file.
	 *
	 */

	public Query createMultiFieldQueryParser(LiusConfig xc, String searchExp) {
		try {
			MultiFieldQueryParser mqp = new MultiFieldQueryParser(xc.getSearchFields(), AnalyzerFactory.getAnalyzer(xc));
			q = mqp.parse(searchExp);
		}
		catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return q;
	}

	/**
	 *
	 * Création d'un objet Query de type QueryParser à partir du fichier de
	 *
	 * configuration.
	 *
	 * <br/><br/>
	 *
	 * Creation of a Query object of type QueryParser from the configuration
	 * file.
	 *
	 */

	public Query createQueryParser(LiusConfig xc, String searchExp) {

		try {

			QueryParser qp = new QueryParser(xc.getDefaultSearchField(), AnalyzerFactory.getAnalyzer(xc));
			q = qp.parse(searchExp);

		}

		catch (ParseException e) {

			logger.error(e.getMessage());

		}

		return q;

	}

	/**
	 *
	 * Méthode permettant de créer un objet Query en fonction de l'élément
	 *
	 * XML trouvé dans le fichier de configuration.
	 *
	 * <br/><br/>
	 *
	 * Method that creates a Query Object relative to the XML element found in
	 * the
	 *
	 * configuration file.
	 *
	 */

	public Query getQueryFactory(String type, LiusConfig xc, String searchExp) {

		if (type.equals("queryTerm"))

			q = createQueryTerm(xc, searchExp);

		else if (type.equals("queryParser"))

			q = createQueryParser(xc, searchExp);

		else if (type.equals("multiFieldQueryParser"))

			q = createMultiFieldQueryParser(xc, searchExp);

		return q;

	}

	/**
	 *
	 * Création d'un objet Query de type RangeQuery à partir du fichier
	 *
	 * de configuration.
	 *
	 * <br/><br/>
	 *
	 * Creation of a Query object of type RangeQuery from the configuration
	 * file.
	 *
	 */

	public Query createRangeQuery(LiusConfig xc, String lowerSearchExp1,

								  String uppersearchExp2) {

		String[] values = xc.getRangeQueryFileds();

		boolean inclusive = true;

		if (values[1].equalsIgnoreCase("true")) {

			inclusive = true;

		}

		else {

			inclusive = false;

		}

		Term t1 = new Term(values[0], lowerSearchExp1);

		Term t2 = new Term(values[0], uppersearchExp2);

		q = new RangeQuery(t1, t2, inclusive);

		return q;

	}

}