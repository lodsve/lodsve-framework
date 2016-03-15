package lius.config;/* * Licensed to the Apache Software Foundation (ASF) under one or more * contributor license agreements.  See the NOTICE file distributed with * this work for additional information regarding copyright ownership. * The ASF licenses this file to You under the Apache License, Version 2.0 * (the "License"); you may not use this file except in compliance with * the License.  You may obtain a copy of the License at * *      http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */

import java.io.InputStreamReader;import java.io.Reader;import java.util.Date;

/**
 * Classe représentant le bean LiusField
 * <br/><br/>
 * Class representing LiusField bean.
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */
public class LiusField {

  private String name;
  private String xpathSelect;
  private String type;
  private String value;
  private String ocurSep;
  private long dateLong;
  private Date date;
  private String dateFormat;
  private String getMethod;
  private String get;
  private InputStreamReader valueInputStreamReader;
  private Reader valueReader;
  private String label;
  private String[] values = null;
  private float boost;    private String fileMimeType;
  private boolean isBoostedB;
  private String fragmenter;

  /**
   * Méthode permettant de récupérer la valeur de name.
   * <br/><br/>
   * Method for getting name value.
   */
  public String getName() {
    return name;
  }

  /** Méthode permettant d'initialiser la  valeur de name.
   * <br/><br/>
   * Method for initializing the name value.
   */
  public void setName(String name) {
    this.name = name;
  }

  /** Méthode permettant de récupérer la valeur de xpathSelect.
   * <br/><br/>
   * Method for getting xpathSelect value.
   */
  public String getXpathSelect() {
    return xpathSelect;
  }

  /**
   * Méthode permettant d'initialiser la  valeur de xpathSelect.
   * <br/><br/>
   * Method for initializing the xpathSelect value.
   */
  public void setXpathSelect(String xpathSelect) {
    this.xpathSelect = xpathSelect;
  }

  /** Méthode permettant de récupérer la valeur de type.
   * <br/><br/>
   * Method for getting type value.
   */
  public String getType() {
    return type;
  }

  /** Méthode permettant d'initialiser la  valeur de type.
   * <br/><br/>
   * Method for initializing the type value.
   */
  public void setType(String type) {
    this.type = type;
  }

  /** Méthode permettant de récupérer la valeur de Value.
   * <br/><br/>
   * Method for getting value value.
   */
  public String getValue() {
    return value;
  }

  /** Méthode permettant d'initialiser la  valeur de value.
   * <br/><br/>
   * Method for initializing the value of value.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /** Méthode permettant de récupérer la valeur du séparateur d'occurance.
   * <br/><br/>
   * Method for getting the hit separator value.
   */
  public String getOcurSep() {
    return ocurSep;
  }

  /** Méthode permettant d'initialiser la  valeur du séparateur d'occurance.
   * <br/><br/>
   * Method for initializing the hit separator value.
   */
  public void setOcurSep(String ocurSep) {
    this.ocurSep = ocurSep;
  }

  /** Méthode permettant d'initialiser la  valeur de date.
   * <br/><br/>
   * Method for initializing the date value.
   */
  public void setDateLong(long dateLong) {
    this.dateLong = dateLong;
  }

  /** Méthode permettant de récupérer la valeur de date.
   * <br/><br/>
   * Method for getting the date value.
   */
  public long getDateLong() {
    return dateLong;
  }

  /** Méthode permettant d'initialiser la  valeur de date.
   * <br/><br/>
   * Method for initializing the date value.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /** Méthode permettant de récupérer la valeur de date.
   * <br/><br/>
   * Method for getting the date value.
   */
  public Date getDate() {
    return date;
  }

  //nouveau tout ce qui suit
  /**Méthode utilsée pour l'indexation du Java Beans.
   *  Elle initialise le nom de la méthode get utilisée avec la réflexion en Java pour
   *  récupérer le contenu du champs*/
  public void setGetMethod(String getMethod) {
    this.getMethod = getMethod;
  }

  /**
   * Permet de récuperer le nom de la méthode get (utilisée pour l'indexation du Java
   * Bean) à partir du fichier de configuration de Lius.
   * <br/><br/>
   * Gets the get method (used for JavaBean indexation) from Lius configuration file.
   */
  public String getGetMethod() {
    return getMethod;
  }

  public void setGet(String get) {
    this.get = get;
  }

  public String getGet() {
    return get;
  }

  /**
   * Utilisé pour intialiser le InputStreamReader pour l'indexation du PDF.
   * <br/><br/>
   * Uses for initializing the InputStreamReader for PDF indexation.
   */
  public void setValueInputStreamReader(InputStreamReader
                                        valueInputStreamReader) {
    this.valueInputStreamReader = valueInputStreamReader;
  }

  /**
   * Permet de récupérer le InputStreamReader utilisé pour l'indexation du PDF.
   * <br/><br/>
   * Method for getting the InputStreamReader used for PDF indexation.
   */
  public InputStreamReader getValueInputStreamReader() {
    return valueInputStreamReader;
  }

  /**
   * Utilisé pour intialiser le Reader pour l'indexation du HTML.
   * <br/><br/>
   * Used for initializing the Reader for HTML indexation.
   */
  public void setValueReader(Reader valueReader) {
    this.valueReader = valueReader;
  }

  /**
   * Permet de récupérer le Reader utilisé pour l'indexation du HTML.
   * <br/><br/>
   * Method for getting the Reader used for HTML indexation.
   */
  public Reader getValueReader() {
    return valueReader;
  }

  /**
   * Permet d'initialiser le label du résultat de recherche à partir du
   * fichier de configuration.
   * <br/><br/>
   * Initialize the label of search result from configuration file.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Permet de récupérer le label du résultat de recherche à partir du
   * fichier de configuration.
   * <br/><br/>
   * Gets the search result label from configuration file.
   *
   */
  public String getLabel() {
    return label;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public void setBoost(float boost) {
    this.boost = boost;
  }

  public float getBoost() {
    return boost;
  }

  public void setIsBoosted(boolean isBoostedB) {
    this.isBoostedB = isBoostedB;
  }


  public boolean getIsBoosted(){
    return isBoostedB;
  }

  public void setFragmenter(String fragmenter){
    this.fragmenter=fragmenter;
  }

  public String getFragmenter(){
    return fragmenter;
  }public String getFileMimeType() {	return fileMimeType;}public void setFileMimeType(String fileMimeType) {	this.fileMimeType = fileMimeType;}public String[] getValues() {	return values;}public void setValues(String[] values) {	this.values = values;}

}