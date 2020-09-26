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
package lodsve.filesystem.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件类型枚举项.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public enum FileTypeEnum {
    /**
     * 文件类型枚举项
     */
    BMP("bmp", "image/bmp"),
    GIF("gif", "image/gif"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    PNG("png", "image/png"),
    TXT("txt", "text/plain"),
    XML("xml", "text/xml"),
    PDF("pdf", "application/pdf"),
    HTML("html", "text/html"),
    XLS("xls", "application/vnd.ms-excel"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    DOC("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPT("ppt", "application/vnd.ms-powerpoint"),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    ZIP("zip", "application/x-zip-compressed"),
    RAR("rar", "application/octet-stream"),
    SEVEN_Z("7z", "application/x-7z-compressed"),
    MP3("mp3", "audio/mp3"),
    THREE_GP("3gp", "video/3gpp"),
    AMR("amr", "audio/amr"),
    MP4("mp4", "video/mpeg4"),
    AVI("avi", "video/avi"),
    RMVB("rmvb", "application/vnd.rn-realmedia-vbr");

    /**
     * 文件后缀
     */
    private final String fileType;
    /**
     * 文件后缀对应的content type
     */
    private final String contentType;

    FileTypeEnum(String fileType, String contentType) {
        this.fileType = fileType;
        this.contentType = contentType;
    }

    public static FileTypeEnum eval(String fileType) {
        FileTypeEnum[] enums = FileTypeEnum.values();
        for (FileTypeEnum e : enums) {
            if (StringUtils.equalsIgnoreCase(fileType, e.getFileType())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("Can't get FileTypeEnum via given fileType: '[%s]'!", fileType));
    }

    public String getFileType() {
        return fileType;
    }

    public String getContentType() {
        return contentType;
    }
}
