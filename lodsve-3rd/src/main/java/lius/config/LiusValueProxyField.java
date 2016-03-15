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



/**
 * Class: LiusValueProxyField <br>
 * 
 * This class provides the possibility to add multiple (equal) LiusFields with
 * different values to the same document.
 * 
 * Changelog:
 * <ul>
 * <li>02.06.2005: Initial implementation (jf)</li>
 * </ul>
 * 
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 * 
 * @see de.teamskill.lius.index.VCardIndexer
 */
public class LiusValueProxyField extends LiusProxyField {

	/**
	 * <code>value</code> is the only field provided by this proxy. Everything
	 * else is inherited from LiusProxyField and is simply passed through to the
	 * 'real' LiusField.
	 */
	private String proxyValue;

	public LiusValueProxyField( LiusField lf ) {
		super( lf );
	}

	public String getValue() {
		return proxyValue;
	}

	public void setValue( String proxyValue ) {
		this.proxyValue = proxyValue;
	}
}