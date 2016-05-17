package lodsve.base.utils;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.ExifReader;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 图片处理工具类(需要整理)
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-17 上午03:06:14
 */
public class ImageUtils {
    /**
     * EXIF版本
     */
    public static final String EXIF_VERSION = "exif_version";
    /**
     * 相机品牌
     */
    public static final String CAMERA_PRODUCT = "camera_product";
    /**
     * 相机型号
     */
    public static final String CAMERA_VERSION = "camera_version";
    /**
     * 光 圈 值
     */
    public static final String APERTURE_VALUE = "aperture_value";
    /**
     * 快门
     */
    public static final String SHUTTER = "shutter";
    /**
     * 感 光 度
     */
    public static final String PHOTOSENSITIVE_DEGREE = "photosensitive_degree";
    /**
     * 软件
     */
    public static final String SOFTWARE_VERSION = "software_version";
    /**
     * 原始拍摄时间
     */
    public static final String ORIGINAL_TIME_TAKEN = "original_time_taken";
    /**
     * 数字化时间
     */
    public static final String DIGITIZED_TIME = "digitized_time";
    /**
     * 方向
     */
    public static final String CAMERA_ORIENTATION = "camera_orientation";
    /**
     * 宽度
     */
    public static final String PHOTO_WIDTH = "photo_width";
    /**
     * 高度
     */
    public static final String PHOTO_HEIGHT = "photo_height";
    /**
     * 水平分辨率
     */
    public static final String X_RESOLUTION = "x_resolution";
    /**
     * 垂直分辨率
     */
    public static final String Y_RESOLUTION = "y_resolution";
    /**
     * 曝光偏差
     */
    public static final String EXPOSURE_BIAS = "exposure_bias";

    /**
     * 私有化构造器
     */
    private ImageUtils() {
    }

    /**
     * 按宽的比例更改图片的大小
     *
     * @param filePath 图片路径
     * @param width    需要改变图片的宽度
     * @param destPath 目标路径
     * @return
     * @throws Exception
     */
    public static File getRatioWidth(String filePath, int width, String destPath) throws Exception {
        File f = new File(filePath);
        BufferedImage bi = ImageIO.read(f);
        double wRatio = (new Integer(width)).doubleValue() / bi.getWidth(); // 宽度的比例
        int height = (int) (wRatio * bi.getHeight()); // 图片转换后的高度
        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(wRatio, wRatio), null); // 设置图像的缩放比例
        image = op.filter(bi, null);
        File zoomFile = new File(destPath);
        File file = null;
        try {
            ImageIO.write((BufferedImage) image, "jpg", zoomFile);
            file = new File(zoomFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 按高的比例更改图片大小
     *
     * @param filePath 图片路径
     * @param height   需要改变图片的高度
     * @param destPath 目标路径
     * @return
     * @throws Exception
     */
    public static File getRatioHeight(String filePath, int height, String destPath) throws Exception {
        File f = new File(filePath);
        BufferedImage bi = ImageIO.read(f);
        double hRatio = (new Integer(height)).doubleValue() / bi.getHeight(); // 高度的比例
        int width = (int) (hRatio * bi.getWidth()); // 图片转换后的高度
        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(hRatio, hRatio), null); // 设置图像的缩放比例
        image = op.filter(bi, null);
        File zoomFile = new File(destPath);
        File file = null;
        try {
            ImageIO.write((BufferedImage) image, "jpg", zoomFile);
            file = new File(zoomFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 按输入的任意宽高改变图片的大小
     *
     * @param filePath 图片路径
     * @param width    需要改变图片的宽度
     * @param height   需要改变图片的高度
     * @param destPath 目标路径
     * @return
     * @throws Exception
     */
    public static File getFixedIcon(String filePath, int width, int height, String destPath) throws Exception {
        File f = new File(filePath);
        BufferedImage bi = ImageIO.read(f);
        double wRatio = (new Integer(width)).doubleValue() / bi.getWidth(); // 宽度的比例
        double hRatio = (new Integer(height)).doubleValue() / bi.getHeight(); // 高度的比例
        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 设置图像的缩放大小
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(wRatio, hRatio), null); // 设置图像的缩放比例
        image = op.filter(bi, null);
        File zoomFile = new File(destPath);
        File file = null;
        try {
            ImageIO.write((BufferedImage) image, "jpg", zoomFile);
            file = new File(zoomFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 根据图像实际宽高和给定宽高，通过比例计算应该得到图像的宽高
     *
     * @param width      实际图像宽度
     * @param height     实际图像高度
     * @param destWidth  给定宽度
     * @param destHeight 给定高度
     * @return new int[]{应得图像宽度, 应得图像高度}
     */
    public static int[] getSizeByPercent(int width, int height, int destWidth, int destHeight) {
        int w = 0;
        int h = 0;
        /**
         * 目标图像宽与源图像宽的比例
         * 目标图像高与源图像高的比例
         */
        double wPercent = (double) destWidth / width;
        double hPercent = (double) destHeight / height;

        if (wPercent > hPercent) {
            wPercent = hPercent;
            w = (int) (wPercent * width);
            h = destHeight;
        } else {
            hPercent = wPercent;
            w = destWidth;
            h = (int) (hPercent * height);
        }

        return new int[]{w, h};
    }

    /**
     * 给图片添加文字水印
     *
     * @param filePath         需要添加水印的图片的路径
     * @param markContent      水印的文字
     * @param markContentColor 水印文字的颜色
     * @param fontSize         字体大小
     * @param location         位置(1、左上角；2、右上角；3、右下角；4、左下角；5、中间)
     * @return 布尔类型
     * @throws Exception
     */
    public static boolean addStringMark(String filePath, String markContent, Color markContentColor, int fontSize,
                                        int location) throws Exception {
        ImageIcon imgIcon = new ImageIcon(filePath);
        Image theImg = imgIcon.getImage();
        int width = theImg.getWidth(null);
        int height = theImg.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();
        g.setColor(markContentColor);
        g.setBackground(Color.white);
        g.drawImage(theImg, 0, 0, null);
        g.setFont(new Font("楷体", Font.PLAIN, fontSize)); // 字体、字型、字号

        int length = 0;
        char[] tmp = markContent.toCharArray();
        String en = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()-_+=[]{};:'\",.<>?0123456789";
        String flag_cn = "·～！￥×（）——『』【】；：‘“，。？《》";
        for (char t : tmp) {
            if (org.apache.commons.lang.StringUtils.contains(en, t)) {
                length += fontSize / 2;
            } else if (org.apache.commons.lang.StringUtils.contains(flag_cn, t)) {
                length += fontSize;
            } else {
                length += fontSize;
            }
        }
        length += 10;

        int w, h;
        switch (location) {
            case 1:
                w = 5;
                h = fontSize;
                break;
            case 2:
                w = width - length;
                h = fontSize;
                break;
            case 3:
                w = width - length;
                h = height - 10;
                break;
            case 4:
                w = 5;
                h = height - 10;
                break;
            case 5:
                w = (width - length) / 2;
                h = (height - fontSize) / 2;
                break;
            default:
                throw new Exception("没有位置！");
        }
        g.drawString(markContent, w, h); // 画文字
        g.dispose();

        return addMark(filePath, bimage);
    }

    /**
     * 给图片添加图像水印
     *
     * @param filePath 需要添加水印的图片的路径
     * @param markPath 水印图片路径
     * @param location 位置(1、左上角；2、右上角；3、右下角；4、左下角；5、中间)
     * @return 布尔类型
     * @throws Exception
     */
    public static boolean addImageMark(String filePath, String markPath, int location) throws Exception {
        // 要处理的原始图片
        ImageIcon icoInput = new ImageIcon(filePath);
        Image imgInput = icoInput.getImage();
        int width = imgInput.getWidth(null);
        int height = imgInput.getHeight(null);
        BufferedImage buffInput = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 要添加上来的水印
        ImageIcon icoADD = new ImageIcon(markPath);
        Image imgADD = icoADD.getImage();
        int w = imgADD.getWidth(null);
        int h = imgADD.getHeight(null);
        // 绘图
        Graphics2D g = buffInput.createGraphics();
        g.drawImage(imgInput, 0, 0, null);

        int x, y;
        switch (location) {
            case 1:
                x = 10;
                y = 10;
                break;
            case 2:
                x = width - w - 10;
                y = 10;
                break;
            case 3:
                x = width - w - 10;
                y = height - h - 10;
                break;
            case 4:
                x = 10;
                y = height - h - 10;
                break;
            case 5:
                x = (width - w) / 2;
                y = (height - h) / 2;
                break;
            default:
                throw new Exception("没有位置！");
        }
        // 下面代码的前面五个参数：图片，x坐标，y坐标,图片宽度,图片高度
        g.drawImage(imgADD, x, y, w, h, null);

        g.dispose();

        return addMark(filePath, buffInput);
    }

    private static boolean addMark(String filePath, BufferedImage buffInput) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder
                    .getDefaultJPEGEncodeParam(buffInput);
            param.setQuality(7, true);
            encoder.encode(buffInput, param);
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将颜色转换成RGB值
     *
     * @param color 必须是#123456这样的格式
     * @return
     */
    public static int[] converColorToRGB(String color) {
        if (StringUtils.isEmpty(color)) {
            return null;
        }
        if (color.startsWith("#")) {
            color = color.substring(1);
        }
        String r = color.substring(0, 2);
        String g = color.substring(2, 4);
        String b = color.substring(4, 6);
        int red = NumberUtils.hex16To10(r);
        int green = NumberUtils.hex16To10(g);
        int blue = NumberUtils.hex16To10(b);

        return new int[]{red, green, blue};
    }

    /**
     * 获取灰化后的图片
     *
     * @param image 原图片
     * @return
     */
    public static Image getGrayImage(Image image) {
        Image grayImage = null;

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int[] pixels = new int[w * h];
        getPixels(image, 0, 0, w, h, pixels);
        grayImage = createImage(new MemoryImageSource(w, h, pixels, 0, w));
        return grayImage;
    }

    /**
     * 获取原图灰化后每个像素上的RGB值(核心灰化算法)
     *
     * @param image  原图
     * @param x      x轴
     * @param y      y轴
     * @param w      宽度
     * @param h      高度
     * @param pixels RGB点阵
     */
    private static void getPixels(Image image, int x, int y, int w, int h, int[] pixels) {
        PixelGrabber pg = new PixelGrabber(image, x, y, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.print("Interrupted waiting for pixels!");
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                int l = pixels[(i * w + j)];
                if (l != 0) {
                    int gray = (int) ((l >> 16 & 0xFF) * 0.29999999999999999D);
                    gray += (int) ((l >> 8 & 0xFF) * 0.58999999999999997D);
                    gray += (int) ((l & 0xFF) * 0.11D);
                    pixels[(i * w + j)] = (0xFF000000 | gray << 16 | gray << 8 | gray);
                } else {
                    pixels[(i * w + j)] = -65794;
                }
            }
    }

    /**
     * Creates an image from the specified image producer.
     *
     * @param producer the image producer
     * @return the image produced
     * @since JDK1.0
     */
    private static Image createImage(ImageProducer producer) {
        return Toolkit.getDefaultToolkit().createImage(producer);
    }

    /**
     * 获取图片的EXIF信息
     *
     * @param file 图片文件
     * @return
     */
    public static Map<String, Object> getImageEXIF(File file) throws FileNotFoundException, JpegProcessingException {
        if (file == null || !file.exists()) {
            return Collections.emptyMap();
        }

        return getImageEXIF(new FileInputStream(file));
    }

    /**
     * 获取图片的EXIF信息
     *
     * @param filePath 图片文件路径
     * @return
     */
    public static Map<String, Object> getImageEXIF(String filePath) throws FileNotFoundException, JpegProcessingException {
        if (StringUtils.isEmpty(filePath)) {
            return Collections.emptyMap();
        }

        return getImageEXIF(new File(filePath));
    }

    /**
     * 获取图片的EXIF信息(key值上在类头定义的)
     *
     * @param is 图片文件流
     * @return
     */
    public static Map<String, Object> getImageEXIF(InputStream is) throws JpegProcessingException {
        if (is == null || !(is instanceof FileInputStream)) {
            return Collections.emptyMap();
        }
        ExifReader er = new ExifReader(is);
        Metadata exif = er.extract();
        Iterator itr = exif.getDirectoryIterator();

        int i = 1;
        Map<String, Object> parmas = new HashMap<String, Object>();
        while (itr.hasNext() && i <= 1) {
            Directory directory = (Directory) itr.next();

            parmas.put(EXIF_VERSION, directory.getString(ExifDirectory.TAG_EXIF_VERSION));
            parmas.put(CAMERA_PRODUCT, directory.getString(ExifDirectory.TAG_MAKE));
            parmas.put(CAMERA_VERSION, directory.getString(ExifDirectory.TAG_MODEL));
            parmas.put(APERTURE_VALUE, directory.getString(ExifDirectory.TAG_FNUMBER));
            parmas.put(SHUTTER, directory.getString(ExifDirectory.TAG_EXPOSURE_TIME));
            parmas.put(PHOTOSENSITIVE_DEGREE, directory.getString(ExifDirectory.TAG_ISO_EQUIVALENT));
            parmas.put(SOFTWARE_VERSION, directory.getString(ExifDirectory.TAG_SOFTWARE));
            parmas.put(ORIGINAL_TIME_TAKEN, directory.getString(ExifDirectory.TAG_DATETIME_ORIGINAL));
            parmas.put(DIGITIZED_TIME, directory.getString(ExifDirectory.TAG_DATETIME_DIGITIZED));
            parmas.put(CAMERA_ORIENTATION, directory.getString(ExifDirectory.TAG_ORIENTATION));
            parmas.put(PHOTO_WIDTH, directory.getString(ExifDirectory.TAG_EXIF_IMAGE_WIDTH));
            parmas.put(PHOTO_HEIGHT, directory.getString(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT));
            parmas.put(X_RESOLUTION, directory.getString(ExifDirectory.TAG_X_RESOLUTION));
            parmas.put(Y_RESOLUTION, directory.getString(ExifDirectory.TAG_Y_RESOLUTION));
            parmas.put(EXPOSURE_BIAS, directory.getString(ExifDirectory.TAG_EXPOSURE_BIAS));

            i++;
        }

        return parmas;
    }

}  