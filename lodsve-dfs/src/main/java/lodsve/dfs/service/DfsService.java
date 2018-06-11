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

package lodsve.dfs.service;

import java.io.File;

/**
 * 文件服务器操作.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-4-0004 10:51
 */
public interface DfsService {
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
