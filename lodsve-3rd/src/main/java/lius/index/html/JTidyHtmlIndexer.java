package lius.index.html;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lius.config.LiusField;
import lius.index.Indexer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;


/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */
public class JTidyHtmlIndexer extends Indexer{

	private Node root = null;

	public int getType() {
		return 1;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if(getLiusConfig().getHtmlFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getHtmlFields();
	}


	public String getContent() {
		if(root == null)
			root = getRoot(getStreamToIndex());
		return getTextContent(root);
	}


	public Collection getPopulatedLiusFields() {
		Collection coll = new ArrayList();
		Iterator it = getLiusConfig().getHtmlFields().iterator();
		while (it.hasNext()) {
			Object field = it.next();
			if (field instanceof LiusField) {
				LiusField lf = (LiusField) field;
				if (lf.getGet() != null) {
					if (lf.getGet().equalsIgnoreCase("content")) {
						String content = getContent();
						lf.setValue(content);
					}
					else{
						if(root != null)
							lf = getElementByName((Element)root,lf.getGet());
						else
							lf = getElementByName((Element)getRoot(getStreamToIndex()),lf.getGet());
					}
					coll.add(lf);
				}
			}
			else{
				coll.add(field);
			}
		}
		return coll;
	}



	private Node getRoot(InputStream is){
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		org.w3c.dom.Document doc = tidy.parseDOM(is,null);
		return doc.getDocumentElement();
	}

	private LiusField getElementByName(Element root, String elementName){
		if(root == null){
			return null;
		}
		LiusField lf = new LiusField();
		NodeList children = root.getElementsByTagName(elementName);
		if(children.getLength()>0){
			if(children.getLength() == 1){
				Element node = (Element) children.item(0);
				Text txt = (Text)node.getFirstChild();
				if(txt != null){
					lf.setName(elementName);
					lf.setValue(txt.getData());

				}
			}
			else{
				String[] values = new String [100];
				for(int i=0; i<children.getLength(); i++){
					Element node = (Element) children.item(i);
					Text txt = (Text)node.getFirstChild();
					if(txt != null){
						values[i]=txt.getData();
					}
				}
				if(values.length>0){
					lf.setName(elementName);
					lf.setValues(values);
				}
			}
		}
		return lf;
	}


	private String getTextContent(Node node){
		NodeList children = node.getChildNodes();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<children.getLength(); i++){
			Node child = children.item(i);
			switch(child.getNodeType()){
				case Node.ELEMENT_NODE:
					sb.append(getTextContent(child));
					sb.append(" ");
					break;
				case Node.TEXT_NODE:
					sb.append(((Text) child).getData());
					break;
			}
		}
		return sb.toString();
	}

}
