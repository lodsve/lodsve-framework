package lodsve.filesystem.handler;

import lodsve.filesystem.properties.FileSystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 公共.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public abstract class AbstractFileSystemHandler implements FileSystemHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFileSystemHandler.class);
    protected final FileSystemProperties properties;

    protected AbstractFileSystemHandler(FileSystemProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建临时目录
     *
     * @param tempFolderPath 临时目录
     * @return 临时目录
     */
    public File createTempFolder(String tempFolderPath) {
        File tempFolder = new File(tempFolderPath);
        if (!tempFolder.exists()) {
            if (!tempFolder.mkdirs()) {
                // 系统临时文件夹
                tempFolder = new File(System.getProperty("java.io.tmpdir"));
                logger.error("创建临时文件夹失败！");
            }
        }

        return tempFolder;
    }

    @Override
    public String downloadFileForStream(String objectName) {
        File tempFolder = createTempFolder(properties.getTempFilePath());

        logger.info("download file : " + tempFolder.getAbsolutePath() + "/" + objectName);
        File fileTemp = new File(tempFolder, objectName);

        download(objectName, fileTemp);
        return fileTemp.getAbsolutePath();
    }

    /**
     * 下载文件到指定位置
     *
     * @param objectName   需要下载的文件
     * @param downloadFile 指定位置
     */
    public abstract void download(String objectName, File downloadFile);

}
