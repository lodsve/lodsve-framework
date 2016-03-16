package lius.index;

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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.sty.io.mimetype.MimeType;
import de.sty.io.mimetype.MimeTypeResolver;

/**
 * @author Rida Benjelloun (ridabenjelloun@gmail.com)
 */
public class MimeTypeUtils {

	public static String getMimeType(InputStream is){
		List ls = MimeTypeResolver.resolve(is);
		MimeType mime = (MimeType)ls.get(0);
		return  mime.getName();
	}

	public static String getMimeType(File f){
		List ls = MimeTypeResolver.resolve(f);
		if(ls.isEmpty()){
			return null;
		}
		MimeType mime = (MimeType)ls.get(0);
		return  mime.getName();
	}

	public static String getMimeType(URL url){
		List ls = MimeTypeResolver.resolve(url);
		if(ls.isEmpty()){
			return null;
		}
		MimeType mime = (MimeType)ls.get(0);
		return  mime.getName();
	}

}
