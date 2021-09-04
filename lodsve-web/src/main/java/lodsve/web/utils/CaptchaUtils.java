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
package lodsve.web.utils;

import lodsve.core.utils.StringUtils;
import lodsve.web.mvc.properties.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-5-10 下午10:04
 */
public class CaptchaUtils {
    private static ServerProperties serverProperties;

    protected static void setServerProperties(ServerProperties serverProperties) {
        CaptchaUtils.serverProperties = serverProperties;
    }

    /**
     * 生成验证码图片文件
     *
     * @param length  验证码数字长度
     * @param request request
     * @return 验证码图片
     */
    public static BufferedImage buildCode(int length, HttpServletRequest request) {
        length = (length <= 0 ? 4 : length);
        //内存中创建图像
        int width = length * 15, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //获取图像上下文
        java.awt.Graphics graphics = image.getGraphics();

        //随机类
        Random random = new Random();

        //设置背景色
        graphics.setColor(getRandomColor(200, 250));
        graphics.fillRect(0, 0, width, height);

        //设置字体
        graphics.setFont(new Font("Times New Roman", java.awt.Font.PLAIN, 18));

        //随机产生干扰线
        graphics.setColor(getRandomColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }

        // 取随机产生的认证码(4位数字)
        String sRand = "";
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            // 将认证码显示到图象中
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            graphics.drawString(rand, 13 * i + 6, 16);
        }

        // 图象生效
        graphics.dispose();

        // 将认证码存入SESSION
        // 默认的session key
        if (request != null) {
            request.getSession().setAttribute(serverProperties.getCaptchaKey(), sRand);
        }

        return image;
    }

    private static Color getRandomColor(int fontColor, int backColor) {
        Random random = new Random();
        if (fontColor > 255) {
            fontColor = 255;
        }
        if (backColor > 255) {
            backColor = 255;
        }

        int red = fontColor + random.nextInt(backColor - fontColor);
        int green = fontColor + random.nextInt(backColor - fontColor);
        int blue = fontColor + random.nextInt(backColor - fontColor);

        return new Color(red, green, blue);
    }

    /**
     * 校验验证码
     *
     * @param request request
     * @param text    用户输入的验证码
     * @return 校验结果
     */
    public static boolean validate(HttpServletRequest request, String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        Object verificationCode = request.getSession().getAttribute(serverProperties.getCaptchaKey());
        return verificationCode != null && StringUtils.equalsIgnoreCase(text, verificationCode.toString());
    }
}
