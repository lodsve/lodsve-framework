package lius.index.openoffice;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com) 
 */
public class OOIndexer2 extends OOIndexer {

	public org.jdom.Document parse(InputStream is) {
		org.jdom.Document xmlDocContent = new org.jdom.Document();
		org.jdom.Document xmlMeta = new org.jdom.Document();
		try {
			List files = unzip(is);
			SAXBuilder builder = new SAXBuilder();
			builder.setEntityResolver(new OpenOfficeEntityResolver());
			builder.setValidation(false);
			File fileContent = File.createTempFile("tmp", "liusOOContent.xml");
			copyInputStream((InputStream) files.get(0),
					new BufferedOutputStream(new FileOutputStream(fileContent)));
			xmlDocContent = builder.build(fileContent);
			File fileMeta = File.createTempFile("tmp", "liusOOMeta.xml");
			copyInputStream((InputStream) files.get(1),
					new BufferedOutputStream(new FileOutputStream(fileMeta)));
			xmlMeta = builder.build(fileMeta);
			Element rootMeta = xmlMeta.getRootElement();
			Element meta = null;
			List ls = new ArrayList();
			if ((ls = rootMeta.getChildren()).size() > 0) {
				meta = (Element) ls.get(0);
			}
			xmlDocContent.getRootElement().addContent(meta.detach());
			xmlDocContent.getRootElement().addNamespaceDeclaration(NS_DC);
		} catch (JDOMException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return xmlDocContent;
	}

}
