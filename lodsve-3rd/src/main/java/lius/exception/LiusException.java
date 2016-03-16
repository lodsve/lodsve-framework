package lius.exception;

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
 * Classe permettant de gérer les exceptions de Lius.
 * <br/><br/> 
 * Class that manage Lius exceptions. 
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 *
 */

public class LiusException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * Constructeur de la classe, il se base sur le constructeur de la classe
	 *
	 * Exception pour afficher les messages d'erreur
	 *
	 * <br/><br/>
	 *
	 * Constructor of the class. It is based on the constructor of Exception
	 * class to
	 *
	 * print errors messages.
	 *
	 */

	public LiusException(String msg) {

		super(msg);

	}

}