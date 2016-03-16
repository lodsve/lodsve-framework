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


import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

/**
 * Class: LiusProxyField <br>
 *
 * This class simply maps all requests to a 'real' LiusField object.
 * The intention of this class is to serve as an entrypoint for multiple (equal) fields from
 * a single document.
 *
 * Changelog:
 * <ul>
 * <li>02.06.2005: Initial implementation (jf)</li>
 * </ul>
 *
 * @see lius.config.LiusValueProxyField
 * @see lius.index.application.VCardIndexer
 *
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 *
 */
public abstract class LiusProxyField extends LiusField {

	private LiusField lf;

	// Proxy Fields should not be constructed directly
	private LiusProxyField() {
	}

	public LiusProxyField( LiusField realField ) {
		if ( realField == null )
			throw new IllegalArgumentException(
					"A valid LiusField instance is required for LiusProxyField." );
		this.lf = realField;
	}

	public String getName() {
		return lf.getName();
	}

	public void setName( String name ) {
		lf.setName( name );
	}

	public String getXpathSelect() {
		return lf.getXpathSelect();
	}

	public void setXpathSelect( String xpathSelect ) {
		lf.setXpathSelect( xpathSelect );
	}

	public String getType() {
		return lf.getType();
	}

	public void setType( String type ) {
		lf.setType( type );
	}

	public String getValue() {
		return lf.getValue();
	}

	public void setValue( String value ) {
		lf.setValue( value );
	}

	public String getOcurSep() {
		return lf.getOcurSep();
	}

	public void setOcurSep( String ocurSep ) {
		lf.setOcurSep(ocurSep);
	}

	public void setDateLong( long dateLong ) {
		lf.setDateLong(dateLong);
	}

	public long getDateLong() {
		return lf.getDateLong();
	}

	public void setDate( Date date ) {
		lf.setDate( date );
	}

	public Date getDate() {
		return lf.getDate();
	}

	public void setGetMethod( String getMethod ) {
		lf.setGetMethod( getMethod );
	}

	public String getGetMethod() {
		return lf.getGetMethod();
	}

	public void setGet( String get ) {
		lf.setGet( get );
	}

	public String getGet() {
		return lf.getGet();
	}

	public void setValueInputStreamReader(
			InputStreamReader valueInputStreamReader ) {
		lf.setValueInputStreamReader( valueInputStreamReader );
	}

	public InputStreamReader getValueInputStreamReader() {
		return lf.getValueInputStreamReader();
	}

	public void setValueReader( Reader valueReader ) {
		lf.setValueReader( valueReader );
	}

	public Reader getValueReader() {
		return lf.getValueReader();
	}

	public void setLabel( String label ) {
		lf.setLabel(label);
	}

	public String getLabel() {
		return lf.getLabel();
	}

	public String[] getValues() {
		return lf.getValues();
	}

	public void setValues( String[] values ) {
		lf.setValues( values );
	}

	public void setDateFormat( String dateFormat ) {
		lf.setDateFormat( dateFormat );
	}

	public String getDateFormat() {
		return lf.getDateFormat();
	}

	public void setBoost( float boost ) {
		lf.setBoost(boost);
	}

	public float getBoost() {
		return lf.getBoost();
	}

	public void setIsBoosted( boolean isBoostedB ) {
		lf.setIsBoosted(isBoostedB);
	}

	public boolean getIsBoosted() {
		return lf.getIsBoosted();
	}

	public void setFragmenter( String fragmenter ) {
		lf.setFragmenter(fragmenter);
	}

	public String getFragmenter() {
		return lf.getFragmenter();
	}

}