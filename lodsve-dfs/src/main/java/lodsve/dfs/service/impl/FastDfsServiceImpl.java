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

package lodsve.dfs.service.impl;

import org.csource.common.FastDfsConfig;
import org.csource.common.FsException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import lodsve.core.utils.FileUtils;
import lodsve.core.utils.StringUtils;
import lodsve.dfs.configuration.DfsProperties;
import lodsve.dfs.exception.FastDfsException;
import lodsve.dfs.service.DfsService;
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
public class FastDfsServiceImpl implements DfsService {

    @Autowired
    private DfsProperties fsProperties;

    private static final Integer DEFAULT_HTTP_PORT = 80;
    private static TrackerServer trackerServer;
    private static StorageClient client;

    @PostConstruct
    private void init() throws IOException, FsException {
        FastDfsConfig config = fsProperties.getFastDfs();
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
