package lodsve.filesystem.handler;

import com.aliyun.oss.common.utils.BinaryUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.SdkHttpUtils;
import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;
import lodsve.filesystem.properties.FileSystemProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
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
        try {
            // 上传文件 (上传文件流的形式) 并设置未公开
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), file.getFinalFileName(), content, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
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
    public String createFolder(String folder) {
        // 文件夹名
        // 判断文件夹是否存在，不存在则创建
        if (!amazonS3Client.doesObjectExist(properties.getBucketName(), folder)) {
            // 创建文件夹
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), folder, new ByteArrayInputStream(new byte[0]), null).withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
            logger.info("创建文件夹成功");
            // 得到文件夹名
            S3Object object = amazonS3Client.getObject(properties.getBucketName(), folder);
            return object.getKey();
        }
        return folder;
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
        amazonS3Client.setBucketAcl(properties.getBucketName(), CannedAccessControlList.PublicRead);
    }
}
