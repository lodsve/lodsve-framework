package lodsve.filesystem.handler;

import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;

import java.io.IOException;

/**
 * 文件上传、下载操作.
 *
 * @author <a href="mailto:sun.hao278@iwhalecloud.com">sunhao(sun.hao278@iwhalecloud.com)</a>
 */
public interface FileSystemHandler {
    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return Result
     * @throws IOException 文件流异常
     */
    Result upload(FileBean file) throws IOException;

    /**
     * 创建文件夹
     *
     * @param folder 文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    String createFolder(String folder);

    /**
     * 根据objectName删除服务器上的文件,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"platform/test.txt"
     */
    void deleteFile(String objectName);

    /**
     * 判断文件是否存在,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"platform/test.txt"
     * @return 是否存在
     */
    boolean isExist(String objectName);

    /**
     * 获取文件URL(私有桶),fileKey指上传返回值中的key
     *
     * @param fileKey 返回值中的key
     * @return 返回文件URL
     */
    String getUrl(String fileKey);

    /**
     * 获取文件URL(私有桶),fileKey指上传返回值中的key
     *
     * @param fileKey    返回值中的key
     * @param expireTime 失效时间，单位（毫秒）
     * @return 返回文件URL
     */
    String getUrl(String fileKey, Long expireTime);

    /**
     * 获取文件URL(共有桶),fileKey指上传返回值中的key
     *
     * @param fileKey 返回值中的key
     * @return 返回文件URL
     */
    String getOpenUrl(String fileKey);

    /**
     * 流式下载文件,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"platform/test.txt"
     * @return 下载的文件路径
     */
    String downloadFileForStream(String objectName);
}
