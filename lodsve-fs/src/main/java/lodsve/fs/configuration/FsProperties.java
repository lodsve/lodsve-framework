package lodsve.fs.configuration;

import com.alibaba.common.FastDfsConfig;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;

/**
 * 配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 10:46
 */
@ConfigurationProperties(prefix = "lodsve.fs", locations = "file:${params.root}/framework/fs.properties")
public class FsProperties {
    private FastDfsConfig fastDFS;

    public FastDfsConfig getFastDFS() {
        return fastDFS;
    }

    public void setFastDFS(FastDfsConfig fastDFS) {
        this.fastDFS = fastDFS;
    }
}
