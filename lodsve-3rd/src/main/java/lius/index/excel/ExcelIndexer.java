package lius.index.excel;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import lius.config.LiusField;
import lius.index.Indexer;

import org.apache.log4j.Logger;


/**
 * Classe permettant d'indexer des fichiers Excel. 
 * <br/><br/> 
 * Class for indexing Excel documents. 
 * @author Rida Benjelloun (ridabenjelloun@gmail.com) 
 */

public class ExcelIndexer extends Indexer {

	static Logger logger = Logger.getRootLogger();

	public int getType() {
		return 1;
	}

	public boolean isConfigured() {
		boolean ef = false;
		if (getLiusConfig().getExcelFields() != null)
			return ef = true;
		return ef;
	}

	public Collection getConfigurationFields() {
		return getLiusConfig().getExcelFields();
	}

	public String getContent() {
		String content = "";
		StringBuffer sb = new StringBuffer();
		try {
			Workbook workbook = Workbook.getWorkbook(getStreamToIndex());
			Sheet[] sheets = workbook.getSheets();
			for (int i = 0; i < sheets.length; i++) {
				Sheet sheet = sheets[i];
				int nbCol = sheet.getColumns();
				for (int j = 0; j < nbCol; j++) {
					Cell[] cells = sheet.getColumn(j);
					for (int k = 0; k < cells.length; k++) {
						sb.append(cells[k].getContents() + " ");
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (jxl.read.biff.BiffException e) {
			logger.error(e.getMessage());
		}
		content = sb.toString();
		return content;

	}

	public Collection getPopulatedLiusFields() {
		Collection coll = new ArrayList();
		Iterator it = getLiusConfig().getExcelFields().iterator();
		while (it.hasNext()) {
			Object field = it.next();
			if (field instanceof LiusField) {
				LiusField lf = (LiusField) field;
				if (lf.getGet() != null) {
					if (lf.getGet().equalsIgnoreCase("content")) {
						String text = getContent();
						lf.setValue(text);
						coll.add(lf);
					}
				}
			} else {
				coll.add(field);
			}
		}
		return coll;
	}
}
