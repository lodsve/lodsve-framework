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

    /**
     * 7z
     */
    SEVEN_Z("7z", "application/x-7z-compressed"),

    MP3("mp3", "audio/mp3"),

    /**
     * 3gp
     */
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
     * 文件后缀对应的context path
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
