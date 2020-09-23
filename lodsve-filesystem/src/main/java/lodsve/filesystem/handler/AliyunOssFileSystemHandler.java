package lodsve.filesystem.handler;

import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;

import java.io.IOException;

/**
 * .
 *
 * @author <a href="mailto:sun.hao278@iwhalecloud.com">sunhao(sun.hao278@iwhalecloud.com)</a>
 */
public class AliyunOssFileSystemHandler extends CommonFileSystemHandler {
    @Override
    public Result upload(FileBean file) throws IOException {
        return null;
    }

    @Override
    public String createFolder(String folder) {
        return null;
    }

    @Override
    public void deleteFile(String objectName) {

    }

    @Override
    public boolean isExist(String objectName) {
        return false;
    }

    @Override
    public String getUrl(String fileKey) {
        return null;
    }

    @Override
    public String getUrl(String fileKey, Long expireTime) {
        return null;
    }

    @Override
    public String getOpenUrl(String fileKey) {
        return null;
    }

    @Override
    public String downloadFileForStream(String objectName) {
        return null;
    }
}
