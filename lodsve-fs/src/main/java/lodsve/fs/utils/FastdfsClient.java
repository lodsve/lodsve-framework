package lodsve.fs.utils;

import lodsve.core.config.SystemConfig;
import lodsve.core.utils.FileUtils;
import lodsve.core.utils.StringUtils;
import lodsve.fs.fastdfs.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * 文件上传，fastdfs文件服务器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/10/19 下午5:28
 */
@Component
public class FastdfsClient {
    public static final String CONFIG_FILE_NAME = "fdfs_client.properties";

    private static TrackerServer trackerServer;
    private static StorageClient client;

    public FastdfsClient() throws Exception {
        Resource resource = SystemConfig.getFrameworkConfig(CONFIG_FILE_NAME);
        if (!resource.exists()) {
            throw new FastDfsException(109004, String.format("file '%s' is not exist!!", CONFIG_FILE_NAME));
        }

        ClientGlobal.init(resource.getInputStream());
        TrackerClient tracker = new TrackerClient();
        trackerServer = tracker.getConnection();
        StorageServer storageServer = tracker.getStoreStorage(trackerServer);

        client = new StorageClient(trackerServer, storageServer);
    }

    /**
     * 上传文件
     *
     * @param fileBytes 文件字节码
     * @param suffix    文件后缀
     * @return 文件在服务器的路径
     */
    public String upload(byte[] fileBytes, String suffix) {
        Assert.notNull(fileBytes);
        Assert.hasText(suffix);

        String[] results;
        try {
            results = client.upload_file(fileBytes, suffix, null);

            if (results == null) {
                throw new FastDfsException(109001, "upload error!");
            }

            String group_name = results[0];
            String remote_filename = results[1];

            return group_name + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + remote_filename;
        } catch (Exception e) {
            throw new FastDfsException(109001, "upload error!");
        }
    }

    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @return 文件在服务器的路径
     */
    public String upload(File file) {
        Assert.isTrue(file != null && file.exists());

        try {
            return upload(FileUtils.getFileByte(file), FileUtils.getFileExt(file));
        } catch (Exception e) {
            throw new FastDfsException(109001, "upload error!");
        }
    }

    /**
     * 获取文件下载路径
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return 下载完整url
     */
    public String getUrl(String filePath) {
        InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
        String fileUrl = "http://" + inetSocketAddress.getAddress().getHostAddress();
        if (ClientGlobal.g_tracker_http_port != 80) {
            fileUrl += ":" + ClientGlobal.g_tracker_http_port;
        }
        fileUrl += "/" + filePath;

        return fileUrl;
    }

    /**
     * 下载文件
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return 返回文件的字节码
     */
    public byte[] download(String filePath) {
        String[] args = analysisGroupNameAndRemoteFileName(filePath);
        try {
            return client.download_file(args[0], args[1]);
        } catch (Exception e) {
            throw new FastDfsException(109002, String.format("download error!groupName is %s, remoteFileName is %s!", args[0], args[1]));
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return true: 成功，false: 失败
     */
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
