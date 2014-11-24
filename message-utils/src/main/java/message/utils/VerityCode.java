package message.utils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 绘制图形验证码的servlet
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-3-21 下午8:56
 */
public class VerityCode extends HttpServlet{
	private static final long serialVersionUID = -4395293913583955749L;

    /**
     * 验证码在session中默认的ID
     */
    public static final String VERITY_CODE_KEY = "verityCode";
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpeg");
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BufferedImage image = CodeBuilder.buildCode(4, request, VERITY_CODE_KEY);

        // 输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    public void destroy() {
    }
}
