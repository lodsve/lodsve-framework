/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
     * @param objectName folder+fileName 如"test/test.txt"
     */
    void deleteFile(String objectName);

    /**
     * 判断文件是否存在,objectName指上传时指定的folder+fileName
     *
     * @param objectName folder+fileName 如"test/test.txt"
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
     * @param objectName folder+fileName 如"test/test.txt"
     * @return 下载的文件路径
     * @throws IOException 创建目录失败
     */
    String downloadFileForStream(String objectName) throws IOException;
}
