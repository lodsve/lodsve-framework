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
