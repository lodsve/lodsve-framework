package lodsve.filesystem.bean;

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
}
