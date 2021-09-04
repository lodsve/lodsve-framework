/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.web.mvc.commons;

import lodsve.core.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 扩展WebInput，为文件类
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-6-26 上午09:37:31
 */
public class FileWebInput extends WebInput {
    private static final Log log = LogFactory.getLog(FileWebInput.class);

    MultipartHttpServletRequest multipartRequest;

    public FileWebInput(HttpServletRequest request) {
        super(request);
        //判断request是否是MultipartHttpServletRequest
        if (request instanceof MultipartHttpServletRequest) {
            multipartRequest = (MultipartHttpServletRequest) request;
        }
    }

    /**
     * 获取所有的上传文件域的名称
     *
     * @return
     */
    public String[] getNames() {
        List<String> list = new ArrayList<>();
        Iterator it = multipartRequest.getFileNames();
        if (it.hasNext()) {
            list.add((String) it.next());
        }

        int length = list.size();
        if (length < 1) {
            return null;
        }

        return list.toArray(new String[length]);
    }

    /**
     * 保持上传文件域
     *
     * @param name 上传文件域的名称
     * @param file 保持的目标文件
     * @throws IllegalStateException
     * @throws java.io.IOException
     */
    public void saveFile(String name, File file) throws IllegalStateException, IOException {
        if (StringUtils.isEmpty(name) || file == null || !file.exists()) {
            log.error("this two paramters can't be null!and file must be exist!");
            return;
        }
        MultipartFile f = multipartRequest.getFile(name);
        f.transferTo(file);
    }

    /**
     * 保持上传文件域
     *
     * @param name     上传文件域的名称
     * @param fileName 保持的目标文件路径
     * @throws IllegalStateException
     * @throws java.io.IOException
     */
    public void saveFile(String name, String fileName) throws IllegalStateException, IOException {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(fileName)) {
            log.error("this two paramters can't be null!");
            return;
        }
        File destFile = new File(fileName);
        if (!destFile.canWrite() || !destFile.exists()) {
            log.error("file must be exist and can be write!");
            return;
        }
        this.saveFile(name, destFile);
    }

    /**
     * 获取文件域的输入流
     *
     * @param name 上传文件域的名称
     * @return
     * @throws java.io.IOException
     */
    public InputStream getFileInputStream(String name) throws IOException {
        MultipartFile f = multipartRequest.getFile(name);
        return f.getInputStream();
    }

    /**
     * 获取文件域的字符数组
     *
     * @param name 上传文件域的名称
     * @return
     * @throws java.io.IOException
     */
    public byte[] getFileBytes(String name) throws IOException {
        MultipartFile f = multipartRequest.getFile(name);
        return f.getBytes();
    }

    /**
     * 根据域的名称获取上传的文件
     *
     * @param name 上传文件域的名称
     * @return
     */
    public MultipartFile getFile(String name) {
        return multipartRequest.getFile(name);
    }

    /**
     * 将文件保存到一个输出流中
     *
     * @param name 上传文件域的名称
     * @param out  输出流
     * @throws java.io.IOException
     */
    public void copyFileTo(String name, OutputStream out) throws IOException {
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();
        IOUtils.copy(in, out);
    }

    /**
     * 将文件按照给定的编码转成字符串
     *
     * @param name     上传文件域的名称
     * @param encoding 指定编码
     * @return
     * @throws java.io.IOException
     */
    public String copyFileToString(String name, String encoding) throws IOException {
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();

        return IOUtils.toString(in, encoding);
    }

    /**
     * 将文件按照制定编码保存到writer中
     *
     * @param name     上传文件域的名称
     * @param out      writer
     * @param encoding 指定编码
     * @throws java.io.IOException
     */
    public void copyFileToWriter(String name, Writer out, String encoding) throws IOException {
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();

        IOUtils.copy(in, out, encoding);
    }
}
