package lodsve.filesystem.properties;

import com.aliyun.oss.common.comm.Protocol;
import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Data;

/**
 * 文件上传组件配置.
 *
 * @author <a href="mailto:sun.hao278@iwhalecloud.com">sunhao(sun.hao278@iwhalecloud.com)</a>
 */
@ConfigurationProperties(prefix = "lodsve.file-system")
@Data
public class FileSystemProperties {
    /**
     * 使用的文件上传类型
     */
    private FileSystemTypeEnum type = FileSystemTypeEnum.OSS;
    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    private String accessKeySecret;
    /**
     * 使用的桶名称
     */
    private String bucketName;
    /**
     * 临时文件路径，本地
     */
    private String tempFilePath = System.getProperty("java.io.tmpdir");
    /**
     * 阿里云oss配置
     */
    private OssProperties oss = new OssProperties();
    /**
     * 亚马逊云s3配置
     */
    private AwsProperties aws = new AwsProperties();
    /**
     * 客户端扩展配置
     */
    private ClientExtendProperties client = new ClientExtendProperties();

    /**
     * 使用的文件存储类型
     */
    public static enum FileSystemTypeEnum {
        /**
         * 亚马逊云存储
         */
        AWS,
        /**
         * 阿里云存储
         */
        OSS
    }

    /**
     * AWS基本服務端屬性.
     */
    @Data
    public static class AwsProperties {
        /**
         * 区域
         */
        private String region;
    }

    /**
     * OSS基本服務端屬性.
     */
    @Data
    public static class OssProperties {
        /**
         * 上传目标地址
         */
        private String endpoint;
    }

    /**
     * 客户端扩展配置
     */
    @Data
    public static class ClientExtendProperties {
        /**
         * 允许打开的最大HTTP连接数。默认为1024
         */
        private int maxConnections = 1024;
        /**
         * Socket层传输数据的超时时间（单位：毫秒）。默认为50000毫秒
         */
        private int socketTimeout = 50000;
        /**
         * 建立连接的超时时间（单位：毫秒）。默认为50000毫秒
         */
        private int connectionTimeout = 50000;
        /**
         * 从连接池中获取连接的超时时间（单位：毫秒）。默认不超时
         */
        private int connectionRequestTimeout;
        /**
         * 如果空闲时间超过此参数的设定值，则关闭连接（单位：毫秒）。默认为60000毫秒
         */
        private long idleConnectionTime = 60000;
        /**
         * 请求失败后最大的重试次数。默认3次
         */
        private int maxErrorRetry = 3;
        /**
         * 是否支持CNAME作为Endpoint，默认支持CNAME
         */
        private boolean supportCname = true;
        /**
         * 是否开启二级域名（Second Level Domain）的访问方式，默认不开启
         */
        private boolean sldEnabled = false;
        /**
         * 连接OSS所采用的协议（HTTP/HTTPS），默认为HTTP
         */
        private String protocol = Protocol.HTTP.toString();
        /**
         * 用户代理，指HTTP的User-Agent头。默认为”aliyun-sdk-java”
         */
        private String userAgent = "aliyun-sdk-java";
    }

}
