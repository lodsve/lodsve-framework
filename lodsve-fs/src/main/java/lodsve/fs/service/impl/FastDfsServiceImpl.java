package lodsve.fs.service.impl;

import com.alibaba.common.FastDfsConfig;
import com.alibaba.common.FsException;
import com.alibaba.fastdfs.ClientGlobal;
import com.alibaba.fastdfs.StorageClient;
import com.alibaba.fastdfs.StorageClient1;
import com.alibaba.fastdfs.StorageServer;
import com.alibaba.fastdfs.TrackerClient;
import com.alibaba.fastdfs.TrackerServer;
import lodsve.core.utils.FileUtils;
import lodsve.core.utils.StringUtils;
import lodsve.fs.configuration.FsProperties;
import lodsve.fs.exception.FastDfsException;
import lodsve.fs.service.FsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * fast_dfs 实现.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 11:05
 */
public class FastDfsServiceImpl implements FsService {

    @Autowired
    private FsProperties fsProperties;

    private static final Integer DEFAULT_HTTP_PORT = 80;
    private static TrackerServer trackerServer;
    private static StorageClient client;

    @PostConstruct
    private void init() throws IOException, FsException {
        FastDfsConfig config = fsProperties.getFastDFS();
        if (config == null) {
            throw new FastDfsException(109004, "fastdfs config is not exist!!");
        }

        ClientGlobal.init(config);
        TrackerClient tracker = new TrackerClient();
        trackerServer = tracker.getConnection();
        StorageServer storageServer = tracker.getStoreStorage(trackerServer);

        client = new StorageClient(trackerServer, storageServer);
    }

    @Override
    public String upload(byte[] fileBytes, String suffix) {
        Assert.notNull(fileBytes);
        Assert.hasText(suffix);

        String[] results;
        try {
            results = client.upload_file(fileBytes, suffix, null);

            if (results == null) {
                throw new FastDfsException(109001, "upload error!");
            }

            String groupName = results[0];
            String remoteFileName = results[1];

            return groupName + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + remoteFileName;
        } catch (Exception e) {
            throw new FastDfsException(109001, "upload error!");
        }
    }

    @Override
    public String upload(File file) {
        Assert.isTrue(file != null && file.exists());

        try {
            return upload(FileUtils.getFileByte(file), FileUtils.getFileExt(file));
        } catch (Exception e) {
            throw new FastDfsException(109001, "upload error!");
        }
    }

    @Override
    public String getUrl(String filePath) {
        InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
        String fileUrl = "http://" + inetSocketAddress.getAddress().getHostAddress();
        if (ClientGlobal.g_tracker_http_port != DEFAULT_HTTP_PORT) {
            fileUrl += ":" + ClientGlobal.g_tracker_http_port;
        }
        fileUrl += "/" + filePath;

        return fileUrl;
    }

    @Override
    public byte[] download(String filePath) {
        String[] args = analysisGroupNameAndRemoteFileName(filePath);
        try {
            return client.download_file(args[0], args[1]);
        } catch (Exception e) {
            throw new FastDfsException(109002, String.format("download error!groupName is %s, remoteFileName is %s!", args[0], args[1]));
        }
    }

    @Override
    public boolean delete(String filePath) {
        String[] args = analysisGroupNameAndRemoteFileName(filePath);
        try {
            int result = client.delete_file(args[0], args[1]);
            if (result == 0) {
                return true;
            } else {
                throw new FastDfsException(109003, String.format("delete error!groupName is %s, remoteFileName is %s!", args[0], args[1]));
            }
        } catch (Exception e) {
            throw new FastDfsException(109003, String.format("delete error!groupName is %s, remoteFileName is %s!", args[0], args[1]));
        }
    }

    /**
     * 分解groupName和remoteFileName
     *
     * @param filePath 上传之后返回的路径
     * @return result[0]: groupName; result[1]: remoteFileName
     */
    private String[] analysisGroupNameAndRemoteFileName(String filePath) {
        Assert.hasText(filePath);

        String[] temp = StringUtils.split(filePath, StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR);

        String groupName = temp[0];
        String remoteFileName = StringUtils.substringAfter(filePath, groupName + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR);

        return new String[]{groupName, remoteFileName};
    }
}
