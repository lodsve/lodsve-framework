/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.filesystem.handler;

import lodsve.filesystem.bean.FileBean;
import lodsve.filesystem.bean.Result;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * 文件上传、下载操作.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public interface FileSystemHandler extends InitializingBean, DisposableBean {
    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return Result
     * @throws IOException 文件流异常
     */
    Result upload(FileBean file) throws IOException;

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
     * 获取文件URL(私有桶),objectName指上传返回值中的objectName
     *
     * @param objectName 返回值中的objectName
     * @return 返回文件URL
     */
    String getUrl(String objectName);

    /**
     * 获取文件URL(私有桶),objectName指上传返回值中的objectName
     *
     * @param objectName 返回值中的objectName
     * @param expireTime 失效时间，单位（毫秒）
     * @return 返回文件URL
     */
    String getUrl(String objectName, Long expireTime);

    /**
     * 获取文件URL(共有桶),objectName指上传返回值中的objectName
     *
     * @param objectName 返回值中的objectName
     * @return 返回文件URL
     */
    String getOpenUrl(String objectName);

    /**
     * 流式下载文件,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"platform/test.txt"
     * @return 下载的文件路径
     * @throws IOException 创建目录失败
     */
    String downloadFileForStream(String objectName) throws IOException;
}
