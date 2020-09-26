/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.filesystem.handler;

import com.aliyun.oss.*;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;
import lodsve.filesystem.enums.AccessControlEnum;
import lodsve.filesystem.properties.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云oss上传.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Slf4j
public class AliyunOssFileSystemHandler extends AbstractFileSystemHandler {
    /**
     * 阿里OSS内网标识
     */
    public static final String OSS_INTERNAL_TAG = "-internal.";
    private final ClientBuilderConfiguration configuration;
    private OSS client;

    public AliyunOssFileSystemHandler(FileSystemProperties properties, ClientBuilderConfiguration configuration) {
        super(properties);
        this.configuration = configuration;
    }

    @Override
    public Result upload(FileBean file) throws IOException {
        Result result = new Result();
        String fileName = file.getFileName();

        // 以输入流的形式上传文件
        InputStream content = file.getContent();
        // 创建上传Object的MetadataBinaryUtil
        ObjectMetadata metadata = new ObjectMetadata();
        if (file.getValidatorMd5()) {
            String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(IOUtils.toByteArray(content)));
            metadata.setContentMD5(md5);
            result.setMd5(md5);
        }

        // 上传的文件的长度
        metadata.setContentLength(content.available());
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(file.getContentType());
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("attachment;filename=\"" + fileName + "\"");
        // 设置访问权限
        metadata.setObjectAcl(evalAccessControlValue(file.getAccessControl()));
        try {
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = client.putObject(properties.getBucketName(), file.getFinalFileName(), content, metadata);

            // 解析结果
            result.setObjectName(file.getFolder() + "/" + file.getFinalFileName());
            result.setEtag(putResult.getETag());
            result.setFileName(fileName);
            result.setResult(StringUtils.isNotBlank(putResult.getETag()));
            return result;
        } catch (OSSException | ClientException e) {
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteFile(String objectName) {
        client.deleteObject(properties.getBucketName(), objectName);
        log.info("删除" + properties.getBucketName() + "下的文件" + objectName + "成功");
    }

    @Override
    public boolean isExist(String objectName) {
        boolean exist = client.doesObjectExist(properties.getBucketName(), objectName);
        log.info(properties.getBucketName() + "下的文件" + objectName + "存在：" + exist);
        return exist;
    }

    @Override
    public String getUrl(String objectName) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 50);
        // 生成URL
        URL url = client.generatePresignedUrl(properties.getBucketName(), objectName, expiration);
        if (url != null) {
            // 替换OSS内网标识
            return url.toString().replace(OSS_INTERNAL_TAG, ".");
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getUrl(String objectName, Long expireTime) {
        // 设置URL过期时间
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        // 生成URL
        URL url = client.generatePresignedUrl(properties.getBucketName(), objectName, expiration);
        if (url != null) {
            // 替换OSS内网标识
            return url.toString().replace(OSS_INTERNAL_TAG, ".");
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getOpenUrl(String objectName) {
        // http://${bucketName}/${endpointUpload}/${folder}/${fileName}
        String urlSeparation = "/";
        String url = "http://" + properties.getBucketName() + "." + properties.getOss().getEndpoint() + urlSeparation + objectName;
        url = url.replace(OSS_INTERNAL_TAG, ".");
        return url;
    }

    @Override
    public void download(String objectName, File downloadFile) {
        client.getObject(new GetObjectRequest(properties.getBucketName(), objectName), downloadFile);
    }

    @Override
    public void destroy() throws Exception {
        if (null != client) {
            client.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new OSSClientBuilder().build(
            properties.getOss().getEndpoint(),
            properties.getAccessKeyId(),
            properties.getAccessKeySecret(),
            configuration);
    }

    private CannedAccessControlList evalAccessControlValue(AccessControlEnum accessControlEnum) {
        accessControlEnum = (null == accessControlEnum ? AccessControlEnum.DEFAULT : accessControlEnum);
        switch (accessControlEnum) {
            case PRIVATE:
                return CannedAccessControlList.Private;
            case PUBLIC_READ:
                return CannedAccessControlList.PublicRead;
            case PUBLIC_READ_WRITE:
                return CannedAccessControlList.PublicReadWrite;
            case DEFAULT:
            default:
                return CannedAccessControlList.Default;
        }
    }
}
