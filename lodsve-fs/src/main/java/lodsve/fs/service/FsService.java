package lodsve.fs.service;

import java.io.File;

/**
 * 文件服务器操作.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-12-4-0004 10:51
 */
public interface FsService {
    /**
     * 上传文件
     *
     * @param fileBytes 文件字节码
     * @param suffix    文件后缀
     * @return 文件在服务器的路径
     */
    String upload(byte[] fileBytes, String suffix);

    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @return 文件在服务器的路径
     */
    String upload(File file);

    /**
     * 获取文件下载路径
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return 下载完整url
     */
    String getUrl(String filePath);

    /**
     * 下载文件
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return 返回文件的字节码
     */
    byte[] download(String filePath);

    /**
     * 删除文件
     *
     * @param filePath 上传后返回的文件在服务器的路径
     * @return true: 成功，false: 失败
     */
    boolean delete(String filePath);
}
