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
package lodsve.filesystem.enums;

/**
 * OSS权限控制枚举.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public enum AccessControlEnum {
    /**
     * 继承Bucket	文件遵循存储空间的访问权限。
     */
    DEFAULT,
    /**
     * 私有	文件的拥有者和授权用户有该文件的读写权限，其他用户没有权限操作该文件。
     */
    PRIVATE,
    /**
     * 公共读	文件的拥有者和授权用户有该文件的读写权限，其他用户只有文件的读权限。请谨慎使用该权限。
     */
    PUBLIC_READ,
    /**
     * 公共读写	所有用户都有该文件的读写权限。请谨慎使用该权限。
     */
    PUBLIC_READ_WRITE
}
