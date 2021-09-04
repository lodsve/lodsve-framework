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
package lodsve.filesystem.bean;

import lodsve.filesystem.enums.AccessControlEnum;
import lombok.Data;
import lombok.ToString;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 上传文件.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Data
@ToString
public class FileBean implements Serializable {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件流
     */
    private InputStream content;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 是否要检验文件md5值，防止上传过程中发生了异常
     */
    private Boolean validatorMd5;
    /**
     * 文件内容类型 content-type
     */
    private String contentType;
    /**
     * 上传的目录
     */
    private String folder;
    /**
     * 最终要上传的文件名
     */
    private String finalFileName;
    /**
     * 文件访问权限，详见{@link AccessControlEnum}
     */
    private AccessControlEnum accessControl;
}
