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
package lodsve.filesystem.server;

import lodsve.core.utils.EncryptUtils;
import lodsve.core.utils.RandomUtils;
import lodsve.core.utils.Snowflake;
import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;
import lodsve.filesystem.enums.AccessControlEnum;
import lodsve.filesystem.enums.FileTypeEnum;
import lodsve.filesystem.handler.FileSystemHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件系统对外客户端.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class FileSystemServer {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemServer.class);
    private static final Pattern ENCODED_CHARACTERS_PATTERN;

    static {
        StringBuilder pattern = new StringBuilder();
        pattern.append(Pattern.quote("+")).append("|").append(Pattern.quote("*")).append("|").append(Pattern.quote("%7E")).append("|").append(Pattern.quote("%2F"));
        ENCODED_CHARACTERS_PATTERN = Pattern.compile(pattern.toString());
    }

    private final FileSystemHandler fileSystemHandler;

    public FileSystemServer(FileSystemHandler fileSystemHandler) {
        this.fileSystemHandler = fileSystemHandler;
    }

    /**
     * 根据objectName删除服务器上的文件,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"test/test.txt"
     */
    public void deleteFile(String objectName) {
        fileSystemHandler.deleteFile(objectName);
    }

    /**
     * 判断文件是否存在,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"test/test.txt"
     * @return 文件是否存在
     */
    public boolean isExist(String objectName) {
        return fileSystemHandler.isExist(objectName);
    }

    /**
     * 上传至文件服务器，具体使用哪个文件服务器，由lodsve.file-system.type来决定<p/>
     *
     * @param file   上传文件
     * @param folder 文件夹名 如"lodsve-framework/"
     * @return FileDTO 返回文件服务器中的一些参数
     */
    public Result uploadFile(File file, String folder) {
        return uploadFile(file, folder, AccessControlEnum.DEFAULT, false);
    }

    /**
     * 上传至文件服务器，具体使用哪个文件服务器，由lodsve.file-system.type来决定<p/>
     *
     * @param file          上传文件
     * @param folder        文件夹名 如"lodsve-framework/"
     * @param accessControl 文件访问权限，详见{@link AccessControlEnum}
     * @return FileDTO 返回文件服务器中的一些参数
     */
    public Result uploadFile(File file, String folder, AccessControlEnum accessControl) {
        return uploadFile(file, folder, accessControl, false);
    }

    /**
     * 上传至文件服务器，具体使用哪个文件服务器，由lodsve.file-system.type来决定<p/>
     *
     * @param file          上传文件
     * @param folder        文件夹名 如"lodsve-framework/"
     * @param accessControl 文件访问权限，详见{@link AccessControlEnum}
     * @param validatorMd5  是否校验md5,如果校验,则返回md5值
     * @return FileDTO 返回oss返回的对象
     */
    public Result uploadFile(File file, String folder, AccessControlEnum accessControl, boolean validatorMd5) {
        Assert.notNull(file, "上传文件不能为空!");
        Assert.notNull(file.getName(), "上传文件格式不对!");

        try {
            String fileName = file.getName();

            FileBean bean = new FileBean();
            bean.setFileName(fileName);
            bean.setFileSize(file.length());
            bean.setContent(new FileInputStream(file));
            bean.setContentType(getContentType(fileName));
            bean.setAccessControl(null == accessControl ? AccessControlEnum.DEFAULT : accessControl);
            if (!StringUtils.endsWith(folder, File.separator)) {
                folder = folder + File.separator;
            }
            bean.setFolder(folder);
            bean.setValidatorMd5(validatorMd5);
            // 文件命名方式：源文件名+雪花ID+源文件后缀
            // 尽量避免文件重名
            bean.setFinalFileName(folder + getDistFileName(fileName));

            return fileSystemHandler.upload(bean);
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("文件上传失败！");
        }
    }

    private String getDistFileName(String originFileName) throws NoSuchAlgorithmException {
        // 文件命名方式：MD5(源文件名+时间戳+随机数+雪花ID)+源文件后缀
        // 尽量避免文件重名
        String plainText = String.format("%s_%s_%s_%s", FilenameUtils.getBaseName(originFileName), System.currentTimeMillis(), RandomUtils.randomString(4), Snowflake.nextId());
        String encryptText = EncryptUtils.encodeMD5(plainText);

        return String.format("%s", urlEncode(encryptText));
    }

    private String urlEncode(String value) {
        if (value == null) {
            return "";
        } else {
            try {
                String encoded = URLEncoder.encode(value, "UTF-8");
                Matcher matcher = ENCODED_CHARACTERS_PATTERN.matcher(encoded);

                StringBuffer buffer;
                String replacement;
                for (buffer = new StringBuffer(encoded.length()); matcher.find(); matcher.appendReplacement(buffer, replacement)) {
                    replacement = matcher.group(0);
                    if ("+".equals(replacement)) {
                        replacement = "%20";
                    } else if ("*".equals(replacement)) {
                        replacement = "%2A";
                    } else if ("%7E".equals(replacement)) {
                        replacement = "~";
                    }
                }

                matcher.appendTail(buffer);
                return buffer.toString();
            } catch (UnsupportedEncodingException var6) {
                throw new RuntimeException(var6);
            }
        }
    }

    /**
     * 通过文件名[后缀]判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    private String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = FilenameUtils.getExtension(fileName);

        try {
            return FileTypeEnum.eval(fileExtension).getContentType();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件URL(私有桶),fileKey指上传返回值中的key
     *
     * @param objectName 返回值中的objectName
     * @return 返回文件URL
     */
    public String getUrl(String objectName) {
        return fileSystemHandler.getUrl(objectName);
    }

    /**
     * 获取文件URL(私有桶),objectName指上传返回值中的objectName
     *
     * @param objectName 返回值中的objectName
     * @param expireTime 失效时间，单位（毫秒）
     * @return 返回文件URL
     */
    public String getUrl(String objectName, Long expireTime) {
        return fileSystemHandler.getUrl(objectName, expireTime);
    }

    /**
     * 获取文件URL(私有桶),objectName指上传返回值中的objectName
     *
     * @param objectName 返回值中的objectName
     * @return 返回文件URL
     */
    public String getOpenUrl(String objectName) {
        return fileSystemHandler.getOpenUrl(objectName);
    }

    /**
     * 流式下载文件,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"test/test.txt"
     * @return 下载的文件路径
     * @throws IOException 创建目录失败
     */
    public String downloadFileForStream(String objectName) throws IOException {
        return fileSystemHandler.downloadFileForStream(objectName);
    }
}
