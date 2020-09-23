package lodsve.filesystem.provider;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import lodsve.filesystem.properties.FileSystemProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * .
 *
 * @author <a href="mailto:sun.hao278@iwhalecloud.com">sunhao(sun.hao278@iwhalecloud.com)</a>
 */
public class AliyunOssProvider implements Provider<OSS>, InitializingBean, DisposableBean {
    private final FileSystemProperties properties;
    private OSS client;

    public AliyunOssProvider(FileSystemProperties properties) {
        this.properties = properties;
    }

    @Override
    public OSS build() {
        return client;
    }

    @Override
    public void destroy() throws Exception {
        if (null != client) {
            client.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileSystemProperties.ClientExtendProperties clientProperties = properties.getClient();
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setMaxConnections(clientProperties.getMaxConnections());
        configuration.setSocketTimeout(clientProperties.getSocketTimeout());
        configuration.setConnectionTimeout(clientProperties.getConnectionTimeout());
        configuration.setConnectionRequestTimeout(clientProperties.getConnectionRequestTimeout());
        clientProperties.setIdleConnectionTime(clientProperties.getIdleConnectionTime());
        configuration.setMaxErrorRetry(clientProperties.getMaxErrorRetry());
        configuration.setSupportCname(clientProperties.isSupportCname());
        configuration.setSLDEnabled(clientProperties.isSldEnabled());
        if (Protocol.HTTP.toString().equals(clientProperties.getProtocol())) {
            configuration.setProtocol(Protocol.HTTP);
        } else if (Protocol.HTTPS.toString().equals(clientProperties.getProtocol())) {
            configuration.setProtocol(Protocol.HTTPS);
        }
        configuration.setUserAgent(clientProperties.getUserAgent());

        client = new OSSClientBuilder().build(
            properties.getOss().getEndpoint(),
            properties.getAccessKeyId(),
            properties.getAccessKeySecret(),
            configuration);
    }
}
