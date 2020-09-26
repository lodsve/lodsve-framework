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
