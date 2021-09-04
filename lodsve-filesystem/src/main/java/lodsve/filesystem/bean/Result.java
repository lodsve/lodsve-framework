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

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 上传图片返回DTO.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Data
@ToString
public class Result implements Serializable {
    /**
     * 文件唯一标识
     */
    private String objectName;
    /**
     * Object生成时会创建相应的ETag (entity tag) ，ETag用于标示一个Object的内容。
     * 1. 对于PutObject请求创建的Object，ETag值是其内容的MD5值。
     * 2. 对于其他方式创建的Object，ETag值是其内容的UUID。
     */
    private String etag;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件md5值
     */
    private String md5;
    /**
     * 上传结果
     */
    private Boolean result;
}
