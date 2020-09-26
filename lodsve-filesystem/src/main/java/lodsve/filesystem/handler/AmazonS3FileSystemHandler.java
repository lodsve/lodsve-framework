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

import com.aliyun.oss.common.utils.BinaryUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.SdkHttpUtils;
import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;
import lodsve.filesystem.enums.AccessControlEnum;
import lodsve.filesystem.properties.FileSystemProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 亚马逊云s3.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class AmazonS3FileSystemHandler extends AbstractFileSystemHandler {
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3FileSystemHandler.class);
    private final ClientConfiguration awsClientConfiguration;
    private AmazonS3 amazonS3Client;

    public AmazonS3FileSystemHandler(FileSystemProperties properties, ClientConfiguration awsClientConfiguration) {
        super(properties);
        this.awsClientConfiguration = awsClientConfiguration;
    }

    @Override
    public void download(String objectName, File downloadFile) {

    }

    @Override
    public Result upload(FileBean file) throws IOException {
        Result result = new Result();

        // 以输入流的形式上传文件
        InputStream content = file.getContent();
        // 文件名
        String fileName = file.getFileName();

        // 创建上传Object
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (file.getValidatorMd5()) {
            String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(IOUtils.toByteArray(content)));
            objectMetadata.setContentMD5(md5);
            result.setMd5(md5);
        }

        // 指定该Object被下载时的网页的缓存行为
        objectMetadata.setCacheControl("no-cache");
        // 上传的文件的长度
        objectMetadata.setContentLength(content.available());
        // 指定该Object下设置Header
        objectMetadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        objectMetadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentDisposition("attachment;filename=\"" + SdkHttpUtils.urlEncode(fileName, false) + "\"");
        // 设置访问权限
        AccessControlEnum accessControl = file.getAccessControl();
        try {
            // 上传文件 (上传文件流的形式) 并设置未公开
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), file.getFinalFileName(), content, objectMetadata)
                .withCannedAcl(evalAccessControlValue(file.getAccessControl()));
            PutObjectResult putResult = amazonS3Client.putObject(putObjectRequest);

            // 解析结果
            result.setObjectName(file.getFolder() + "/" + file.getFinalFileName());
            result.setEtag(putResult.getETag());
            result.setFileName(fileName);
            result.setResult(StringUtils.isNotBlank(putResult.getETag()));
            return result;
        } catch (SdkClientException e) {
            logger.error("上传亚马逊云S3服务器异常." + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteFile(String objectName) {
        amazonS3Client.deleteObject(properties.getBucketName(), objectName);
        logger.info("删除" + properties.getBucketName() + "下的文件" + objectName + "成功");
    }

    @Override
    public boolean isExist(String objectName) {
        boolean exist = amazonS3Client.doesObjectExist(properties.getBucketName(), objectName);
        logger.info(properties.getBucketName() + "下的文件" + objectName + "存在：" + exist);
        return exist;
    }

    @Override
    public String getUrl(String objectName) {
        URL url = amazonS3Client.getUrl(properties.getBucketName(), objectName);
        return url.toString();
    }

    @Override
    public String getUrl(String objectName, Long expireTime) {
        // 设置URL过期时间
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        URL url = amazonS3Client.generatePresignedUrl(properties.getBucketName(), objectName, expiration);
        return url.toString();
    }

    @Override
    public String getOpenUrl(String objectName) {
        // http://${bucketName}/${endpointUpload}/${folder}/${fileName}
        String urlSeparation = "/";
        return properties.getClient().getProtocol() + "://" + properties.getBucketName() + ".s3." + StringUtils.lowerCase(Regions.fromName(properties.getAws().getRegion()).getName()) +
            ".amazonaws.com" + urlSeparation + objectName;
    }

    @Override
    public void destroy() throws Exception {
        if (amazonS3Client != null) {
            amazonS3Client.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileSystemProperties.AwsProperties awsProperties = properties.getAws();
        BasicAWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKeyId(), properties.getAccessKeySecret());
        amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(awsProperties.getRegion()))
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withClientConfiguration(awsClientConfiguration)
            .build();
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
                return CannedAccessControlList.BucketOwnerFullControl;
        }
    }
}
