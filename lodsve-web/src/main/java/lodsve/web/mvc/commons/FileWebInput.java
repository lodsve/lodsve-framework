/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.web.mvc.commons;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
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
        if(request instanceof MultipartHttpServletRequest){
            multipartRequest = (MultipartHttpServletRequest) request;
        }
    }

    /**
     * 获取所有的上传文件域的名称
     *
     * @return
     */
    public String[] getNames(){
        List<String> list = new ArrayList<>();
        Iterator it = multipartRequest.getFileNames();
        if(it.hasNext()){
            list.add((String)it.next());
        }

        int length = list.size();
        if(length < 1){
            return null;
        }

        return list.toArray(new String[length]);
    }

    /**
     * 保持上传文件域
     *
     * @param name		上传文件域的名称
     * @param file		保持的目标文件
     * @throws IllegalStateException
     * @throws java.io.IOException
     */
    public void saveFile(String name, File file) throws IllegalStateException, IOException{
        if(StringUtils.isEmpty(name) || file == null || !file.exists()){
            log.error("this two paramters can't be null!and file must be exist!");
            return;
        }
        MultipartFile f = multipartRequest.getFile(name);
        f.transferTo(file);
    }

    /**
     * 保持上传文件域
     *
     * @param name			上传文件域的名称
     * @param fileName		保持的目标文件路径
     * @throws IllegalStateException
     * @throws java.io.IOException
     */
    public void saveFile(String name, String fileName) throws IllegalStateException, IOException{
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(fileName)){
            log.error("this two paramters can't be null!");
            return;
        }
        File destFile = new File(fileName);
        if(!destFile.canWrite() || !destFile.exists()){
            log.error("file must be exist and can be write!");
            return;
        }
        this.saveFile(name, destFile);
    }

    /**
     * 获取文件域的输入流
     *
     * @param name			上传文件域的名称
     * @return
     * @throws java.io.IOException
     */
    public InputStream getFileInputStream(String name) throws IOException{
        MultipartFile f = multipartRequest.getFile(name);
        return f.getInputStream();
    }

    /**
     * 获取文件域的字符数组
     *
     * @param name			上传文件域的名称
     * @return
     * @throws java.io.IOException
     */
    public byte[] getFileBytes(String name) throws IOException{
        MultipartFile f = multipartRequest.getFile(name);
        return f.getBytes();
    }

    /**
     * 根据域的名称获取上传的文件
     *
     * @param name			上传文件域的名称
     * @return
     */
    public MultipartFile getFile(String name){
        return multipartRequest.getFile(name);
    }

    /**
     * 将文件保存到一个输出流中
     *
     * @param name			上传文件域的名称
     * @param out			输出流
     * @throws java.io.IOException
     */
    public void copyFileTo(String name, OutputStream out) throws IOException{
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();
        IOUtils.copy(in, out);
    }

    /**
     * 将文件按照给定的编码转成字符串
     *
     * @param name			上传文件域的名称
     * @param encoding		指定编码
     * @return
     * @throws java.io.IOException
     */
    public String copyFileToString(String name, String encoding) throws IOException{
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();

        return IOUtils.toString(in, encoding);
    }

    /**
     * 将文件按照制定编码保存到writer中
     *
     * @param name			上传文件域的名称
     * @param out			writer
     * @param encoding		指定编码
     * @throws java.io.IOException
     */
    public void copyFileToWriter(String name, Writer out, String encoding) throws IOException{
        MultipartFile f = multipartRequest.getFile(name);
        InputStream in = f.getInputStream();

        IOUtils.copy(in, out, encoding);
    }
}
