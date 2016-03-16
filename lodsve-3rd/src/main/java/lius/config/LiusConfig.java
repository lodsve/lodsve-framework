package lius.config;

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



import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Classe représentant le bean LiusConfig <br/><br/>Class representing the
 * LiusConfig bean.
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */

public class LiusConfig {

	private String analyzer;

	private String valueCreateIndex;

	private String mergeFactor;

	private String maxMergeDocs;

	private String optimizeValue;

	private boolean optimize;

	private Collection xmlFileFields;

	private HashMap xmlNodesFields;

	private String[] stopWord;

	private String stopWordSep;

	private String[] searchFields;

	private List displayFields;

	private List browseFieldsToDisplay;

	private String queryTermClass;

	private String termFiled;

	private String defaultSearchField;

	private String elemSearch;

	private String[] rangeQueryFileds;

	private Map javaBeans;

	private Collection pdfFields;

	private Collection msWordFields;

	private Collection htmlFields;

	private Collection excelFields;

	private Collection mixedFields;

	private Collection rtfFields;

	private Collection txtFields;

	private Collection ooFields;

	private Collection pptFields;

	private Collection mp3Fields;

	private Collection texFields;

	private Collection vCardFields;

	private boolean highlight;

	private Collection fontFields;

	private Collection zipFields;

	private Map mimeTypeClass;


	public Map getMimeTypeClass() {
		return mimeTypeClass;
	}

	public void setMimeTypeClass(Map mimeTypeClass) {
		this.mimeTypeClass = mimeTypeClass;
	}

	/**
	 *
	 * Constructeur sans arguments. <br/><br/>Constructor without arguments.
	 *
	 */

	public LiusConfig() {

	}

	/**
	 *
	 * Méthode permettant de récupérer l'analyseur. <br/><br/>Method for
	 *
	 * getting the analyser.
	 *
	 */

	public String getAnalyzer() {

		return analyzer;

	}

	/**
	 *
	 * Méthode permettant d'initialiser l'analyseur. <br/><br/>Method for
	 *
	 * initializing analyser.
	 *
	 */

	public void setAnalyzer(String analyzer) {

		this.analyzer = analyzer;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur de create Index (true, false,
	 *
	 * auto). <br/><br/>Method for initializing the value of createIndex (true,
	 *
	 * false, auto).
	 *
	 */

	public void setValueCreateIndex(String valueCreateIndex) {

		this.valueCreateIndex = valueCreateIndex;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur de create index. <br/><br/>
	 *
	 * Method for getting the value of createIndex.
	 *
	 */

	public String getCreateIndex() {

		return valueCreateIndex;

	}

	/**
	 *
	 * Méthode permettent d'initialiser la valeur d'optimize (valeur permettant
	 *
	 * d'optimiser l'index), <br/><br/>Method for initializing the value of
	 *
	 * optimize (value for optimizing the index).
	 *
	 */

	public void setOptimizeValue(String optimizeValue) {

		this.optimizeValue = optimizeValue;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur pour optimiser l'index (true,
	 *
	 * false) sous forme de chaîne de caractères¸. <br/><br/>Method for getting
	 *
	 * the value of optimize (true, false) as a string.
	 *
	 */

	public String getOptimizeValue() {

		return optimizeValue;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur pour optimiser l'index (true,
	 *
	 * false) sous forme d'une valeur booléenne. <br/><br/>Method for getting
	 *
	 * the value of optimize (true, false) as a boolean.
	 *
	 */

	public boolean getOptimize() {

		if (optimizeValue.equals("true")) {

			optimize = true;

		} else

			optimize = false;

		return optimize;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur de merge factor. <br/><br/>
	 *
	 * Method for getting the value of merge factor.
	 *
	 */

	public String getMergeFactor() {

		return mergeFactor;

	}

	/**
	 *
	 * Méthode permettant d'initiliser la valeur de merge factor. <br/><br/>
	 *
	 * Method for initializing the value of merge factor.
	 *
	 */

	public void setMergeFactor(String mergeFactor) {

		this.mergeFactor = mergeFactor;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur de max merge documents. <br/>
	 *
	 * <br/>Method for getting the value of max merge documents.
	 *
	 */

	public String getMaxMergeDocs() {

		return maxMergeDocs;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur de max merge documents. <br/>
	 *
	 * <br/>Method for intializing the value of max merge documents.
	 *
	 */

	public void setMaxMergeDocs(String maxMergeDocs) {

		this.maxMergeDocs = maxMergeDocs;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur storeFileInLucenDoc, en lui
	 *
	 * passant comme paramètres une collection d'objets de type LiusFields <br/>
	 *
	 * <br/>Method for initializing the value of storeFileInLucenDoc, by passing
	 *
	 * a Collection of LiusFields objects.
	 *
	 */

	public void setXmlFileFields(Collection xmlFileFields) {

		this.xmlFileFields = xmlFileFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer une collection d'objets de type
	 *
	 * LiusField. Cette collection est construite à partir de l'élément
	 *
	 * <storeFileInLucenDoc>dans le fichier de configuration. <br/><br/>Method
	 *
	 * for getting a Collection of LiusField objects. This collection is
	 *
	 * constructed from the element <storeFileInLucenDoc>in the configuration
	 *
	 * file.
	 *
	 */

	public Collection getXmlFileFields() {

		return xmlFileFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur storeEachNodeinLuceneDoc, en
	 *
	 * lui passant comme paramètres un HashMap dont la clé est une expression
	 *
	 * XPath permettant de sélectionner le noeud à placer dans le document
	 *
	 * Lucene, et la valeur (HashMap) une collection d'objets de type LiusField.
	 *
	 * Ce HashMap est construit à partir de l'élément <storeEachNodeinLuceneDoc>
	 *
	 * dans le fichier de configuration. <br/><br/>Method for initializing the
	 *
	 * value storeEachNodeinLuceneDoc by passing it an HashMap which key is an
	 *
	 * XPath expression for selecting the node to place in the Lucene Document
	 *
	 * and the value (HashMap) is a Collection of LiusField objects. This
	 *
	 * HashMap is constructed from the element <storeEachNodeinLuceneDoc>in the
	 *
	 * configuration file.
	 *
	 *
	 *
	 */

	public void setXmlNodesFields(HashMap xmlNodesFields) {

		this.xmlNodesFields = xmlNodesFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur StoreEachNodeinLuceneDoc. Elle
	 *
	 * retourne un HashMap dont la clé est une expression XPath permettant de
	 *
	 * sélectionner le noeud à placer dans le document Lucene, et la valeur (du
	 *
	 * HashMap) une collection d'objets de type LiusField. <br/><br/>Method for
	 *
	 * getting the value storeEachNodeinLuceneDoc. Returns an HashMap which key
	 *
	 * is an XPath expression for selecting the node to place in the Lucene
	 *
	 * Document and the value (HashMap) is a Collection of LiusField objects.
	 *
	 */

	public HashMap getXmlNodesFields() {

		return xmlNodesFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur stopWord. <br/><br/>Method for
	 *
	 * getting the value stopWord.
	 *
	 */

	public String[] getStopWord() {

		return stopWord;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur de stopWord. <br/><br/>
	 *
	 * Method for initializing value stopWord.
	 *
	 */

	public void setStopWord(String[] stopWord) {

		this.stopWord = stopWord;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur stopWordSep. <br/><br/>Method
	 *
	 * for getting the value stopWordSetp.
	 *
	 */

	public String getStopWordSep() {

		return stopWordSep;

	}

	/**
	 *
	 * Méthode permettant d'initialiser la valeur de StopWordSep. <br/><br/>
	 *
	 * Method for initializing the value stopWordSep.
	 *
	 */

	public void setStopWordSep(String stopWordSep) {

		this.stopWordSep = stopWordSep;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur champsRecherche. <br/><br/>
	 *
	 * Method for getting the value searchFields.
	 *
	 */

	public String[] getSearchFields() {

		return searchFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser champsRecherche. <br/><br/>Method for
	 *
	 * initializing the value searchFields.
	 *
	 */

	public void setSearchFields(String[] searchFields) {

		this.searchFields = searchFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur champsAfficher <br/><br/>
	 *
	 * Method for getting the value displayFields.
	 *
	 */

	public List getDisplayFields() {

		return displayFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser champsAfficher. <br/><br/>Method for
	 *
	 * initializing the value displayFields.
	 *
	 */

	public void setDisplayFields(List displayFields) {

		this.displayFields = displayFields;

	}

	// QueryTerm

	/**
	 *
	 * Méthode permettant de récupérer la valeur queryTermClass <br/><br/>
	 *
	 * Method for getting the value queryTermClass.
	 *
	 */

	public String getQueryTermClass() {

		return queryTermClass;

	}

	/**
	 *
	 * Méthode permettant d'initialiser queryTermClass <br/><br/>Method for
	 *
	 * initializing the value queryTermClass.
	 *
	 */

	public void setQueryTermClass(String queryTermClass) {

		this.queryTermClass = queryTermClass;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur termFiled <br/><br/>Method for
	 *
	 * getting the value termFiled.
	 *
	 */

	public String getTermFiled() {

		return termFiled;

	}

	/**
	 *
	 * Méthode permettant d'initialiser termFiled <br/><br/>Method for
	 *
	 * initializing the value termFiled.
	 *
	 */

	public void setTermFiled(String termFiled) {

		this.termFiled = termFiled;

	}

	// Query parser

	/**
	 *
	 * Méthode permettant de récupérer la valeur defaultSearchField <br/><br/>
	 *
	 * Method for getting the value defaultSearchField.
	 *
	 */

	public String getDefaultSearchField() {

		return defaultSearchField;

	}

	/**
	 *
	 * Méthode permettant d'initialiser defaultSearchField <br/><br/>Method for
	 *
	 * initializing the value defaultSearchField.
	 *
	 */

	public void setDefaultSearchField(String defaultSearchField) {

		this.defaultSearchField = defaultSearchField;

	}

	/**
	 *
	 * Méthode permettant d'initialiser elemSearch. <br/><br/>Method for
	 *
	 * initializing the value elemSearch.
	 *
	 */

	public void setElemSearch(String elemSearch) {

		this.elemSearch = elemSearch;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur elemSearch. <br/><br/>Method
	 *
	 * for getting the value elemSearch.
	 *
	 */

	public String getElemSearch() {

		return elemSearch;

	}

	// Range Query

	/**
	 *
	 * Méthode permettant d'initialiser rangeQueryFileds. <br/><br/>Method for
	 *
	 * initializing the value rangeQueryFileds.
	 *
	 */

	public void setRangeQueryFileds(String[] rangeQueryFileds) {

		this.rangeQueryFileds = rangeQueryFileds;

	}

	/**
	 *
	 * Méthode permettant de récupérer la valeur RangeQueryFileds. <br/><br/>
	 *
	 * Method for getting the value rangeQueryFileds.
	 *
	 */

	public String[] getRangeQueryFileds() {

		return rangeQueryFileds;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les proprités du JavaBean qui vont être
	 *
	 * utilisées lors l'indexation à partir du fichier de configuration de Lius.
	 *
	 * <br/><br/>Method for itializing the properties of the JavaBean which
	 *
	 * will be used for indexing from the LiusConfiguration file.
	 *
	 */

	public void setJavaBeansFields(Map javaBeans) {

		this.javaBeans = javaBeans;

	}

	/**
	 *
	 * Méthode permettant de récupérer les propriétés du JavaBeans à partir du
	 *
	 * fichier de configuration de Lius. <br/><br/>Method for getting the
	 *
	 * properties of the JavaBean from the Lius configuration file.
	 *
	 */

	public Map getJavaBeansFields() {

		return javaBeans;

	}

	/**
	 *
	 * Méthode permettant de récupérer les champs PDF utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for getting the PDF fields used during indexation from the Lius
	 *
	 * configuration file.
	 *
	 */

	public Collection getPdfFields() {

		return pdfFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les champs PDF utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius <br/><br/>
	 *
	 * Method for initializing the PDF fields used during indexation from Lius
	 *
	 * configuration file.
	 *
	 */

	public void setPdfFields(Collection pdfFields) {

		this.pdfFields = pdfFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer les champs MS WORD utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius <br/><br/>
	 *
	 * Method for getting the MS WORD fields used during indexation from the
	 *
	 * Lius configuration file.
	 *
	 */

	public Collection getMsWordFields() {

		return msWordFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les champs MS WORD utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius <br/><br/>
	 *
	 * Method for initializing the MS WORD fields used during indexation from
	 *
	 * Lius configuration file.
	 *
	 */

	public void setMsWordFields(Collection msWordFields) {

		this.msWordFields = msWordFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer les champs HTML utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for getting the HTML fields used during indexation from the Lius
	 *
	 * configuration file.
	 *
	 */

	public Collection getHtmlFields() {

		return htmlFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les champs HTML utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for initializing the HTML fields used during indexation from Lius
	 *
	 * configuration file.
	 *
	 */

	public void setHtmlFields(Collection htmlFields) {

		this.htmlFields = htmlFields;

	}




	public Collection getMixedFields() {
		return mixedFields;
	}

	public void setMixedFields(Collection mixedFields) {
		this.mixedFields = mixedFields;
	}

	/**
	 *
	 * Méthode permettant de récupérer les champs pour RTF utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius <br/><br/>
	 *
	 * Method for getting the RTF fields used during indexation from the Lius
	 *
	 * configuration file.
	 *
	 */

	public Collection getRtfFields() {

		return rtfFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les champs RTF utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for initializing the RTF fields used during indexation from Lius
	 *
	 * configuration file.
	 *
	 */

	public void setRtfFields(Collection rtfFields) {

		this.rtfFields = rtfFields;

	}

	/**
	 *
	 * Méthode permettant de récupérer les champs pour Excel utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for getting the Excel fields used during indexation from the Lius
	 *
	 * configuration file.
	 *
	 */

	public Collection getExcelFields() {

		return excelFields;

	}

	/**
	 *
	 * Méthode permettant d'initialiser les champs Excel utilisés lors de
	 *
	 * l'indexation à partir du fichier de configuration de Lius. <br/><br/>
	 *
	 * Method for initializing the Excel fields used during indexation from Lius
	 *
	 * configuration file.
	 *
	 */

	public void setExcelFields(Collection excelFields) {

		this.excelFields = excelFields;

	}

	public void setTxtFields(Collection txtFields) {

		this.txtFields = txtFields;

	}

	public Collection getTxtFields() {

		return txtFields;

	}

	public void setOOFields(Collection ooFields) {

		this.ooFields = ooFields;

	}

	public Collection getOOFields() {

		return ooFields;

	}

	public void setPPTFields(Collection pptFields) {

		this.pptFields = pptFields;

	}

	public Collection getPPTFields() {

		return pptFields;

	}

	public void setMP3Fields(Collection mp3Fields) {

		this.mp3Fields = mp3Fields;

	}

	public Collection getMP3Fields() {

		return mp3Fields;

	}

	public void setTexFields(Collection texFields) {

		this.texFields = texFields;

	}

	public Collection getTexFields() {

		return texFields;

	}

	public void setHighlighter(boolean highlight) {

		this.highlight = highlight;

	}

	public boolean getHighlighter() {

		return highlight;

	}

	public void setVCardFields(Collection vCardFields) {

		this.vCardFields = vCardFields;

	}

	public Collection getVCardFields() {

		return vCardFields;

	}

	public void setFontFields(Collection fontFields) {

		this.fontFields = fontFields;

	}

	public Collection getFontFields() {

		return fontFields;

	}

	public Collection getZipFields() {
		return zipFields;
	}

	public void setZipFields(Collection zipFields) {
		this.zipFields = zipFields;
	}

	public List getBrowseFieldsToDisplay() {
		return browseFieldsToDisplay;
	}

	public void setBrowseFieldsToDisplay(List browseFieldsToDisplay) {
		this.browseFieldsToDisplay = browseFieldsToDisplay;
	}
}