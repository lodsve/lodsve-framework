/*
 * Copyright (C) 2018  Sun.Hao
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件操作的工具类
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-3-16 下午08:25:56
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 系统文件分割符
     */
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 文件大小的单位
     */
    private static final List<String> LIST_FILE_SIZE_UNIT = Arrays.asList("GB", "MB", "KB", "B");

    /**
     * 构造器私有化
     */
    private FileUtils() {
        super();
    }

    /**
     * 根据给定path创建文件夹
     *
     * @param path 给定路径
     */
    public static void createFolder(String path) {
        Assert.hasText(path, "the given path is null!");
        if (logger.isDebugEnabled()) {
            logger.debug("given path is '{}'!", path);
        }

        File file = new File(path);
        createFolder(file);
    }

    /**
     * 根据给定file创建文件夹
     *
     * @param file 给定文件
     */
    public static void createFolder(File file) {
        if (file.exists()) {
            logger.error("folder named '{}' is exist!", file.getName());
            throw new RuntimeException(String.format("'%s'文件夹已存在", file.getName()));
        } else {
            try {
                org.apache.commons.io.FileUtils.forceMkdir(file);
            } catch (IOException e) {
                logger.error("create folder '{}' error!", file.getName());
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folerName 文件名
     * @param path      文件路径
     * @return 删除结果
     */
    public static boolean deleteFolder(String folerName, String path) {
        return deleteFolder(folerName + path);
    }

    /**
     * 删除文件夹
     *
     * @param filePath 路径名+文件名
     * @return 删除结果
     */
    public static boolean deleteFolder(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given filePath '{}' is null!", filePath);
            return false;
        }

        File file = new File(filePath);

        if (!file.exists()) {
            logger.error("this file named '{}' is not exist!", file.getName());
            return false;
        }

        return file.delete();
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @param file 文件字节流
     */
    public static void createFile(String path, byte[] file) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(file);
        } catch (Exception e) {
            logger.error("create file '{}' error!", path);
        }
    }

    /**
     * 重命名文件夹
     *
     * @param oldName 旧文件夹名
     * @param newName 新文件夹名
     * @param path    文件夹路径
     * @return 重命名是否成功
     */
    public static boolean renameFolder(String oldName, String newName, String path) {
        File oldFile = new File(path + FILE_SEPARATOR + oldName);
        return oldFile.renameTo(new File(path + FILE_SEPARATOR + newName));
    }

    /**
     * 如是是文件,删除;如是是文件夹,删除它和它下面的所有文件
     *
     * @param dirPath 文件夹路径
     */
    public static void deleteFiles(String dirPath) {
        File file = new File(dirPath);

        if (!file.isDirectory()) {
            //如果不是文件夹，而是文件，则直接删除
            file.delete();
            return;
        }

        String[] fileNames = file.list();
        if (ArrayUtils.isEmpty(fileNames)) {
            return;
        }

        for (String fileName : fileNames) {
            deleteFiles(dirPath + FILE_SEPARATOR + fileName);
        }

        file.delete();
    }

    /**
     * 获得文件的扩展名,如果是文件夹,返回null.没有扩展名,返回""
     *
     * @param file 要获取信息的文件
     * @return 文件的扩展名
     */
    public static String getFileExt(File file) {
        if (!file.isFile() || !file.exists()) {
            return StringUtils.EMPTY;
        }
        int i = file.getName().lastIndexOf(".");
        if (i == -1) {
            return StringUtils.EMPTY;
        }

        return file.getName().substring(i);
    }

    /**
     * 获得文件的扩展名,如果是文件夹,返回null.没有扩展名,返回""
     *
     * @param fileName 文件名
     * @return 文件的扩展名
     */
    public static String getFileExt(String fileName) {
        Assert.hasLength(fileName, String.format("given fileName '%s' is null!", fileName));
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return StringUtils.EMPTY;
        }

        return fileName.substring(i);
    }

    /**
     * 获取文件名(不包括扩展名)
     *
     * @param fileName 文件名
     * @return 文件名(不包括扩展名)
     */
    public static String getFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            logger.error("given fileName '{}' is null!", fileName);
        }
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return fileName;
        }

        return fileName.substring(0, i);
    }

    /**
     * 获取文件名(不包括扩展名)
     *
     * @param file 要获取信息的文件
     * @return 文件名(不包括扩展名)
     */
    public static String getFileName(File file) {
        if (!file.isFile() || !file.exists()) {
            return StringUtils.EMPTY;
        }

        return getFileName(file.getName());
    }

    /**
     * 根据所给文件获取此文件的字节流
     *
     * @param file 文件
     * @return 文件的字节流
     * @throws IOException io异常
     */
    public static byte[] getFileByte(File file) throws IOException {
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error("given path '{}' is not a file, maybe a folder! or it can not read!", file);
            return null;
        }

        FileInputStream fis = openInputStream(file);
        return IOUtils.toByteArray(fis);
    }

    /**
     * 根据所给路径获取此文件的字节流
     *
     * @param filePath 路径
     * @return 字节流
     * @throws IOException io异常
     */
    public static byte[] getFileByte(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given filePath is null!");
            return null;
        }
        File file = new File(filePath);
        return getFileByte(file);
    }

    /**
     * 根据所给文件获取此文件的字节流
     *
     * @param file 文件
     * @return 文件的字节流
     * @throws IOException io异常
     */
    public static char[] getFileChar(File file) throws IOException {
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error("given path '{}' is not a file, maybe a folder! or it can not read!", file);
            return null;
        }

        FileInputStream fis = openInputStream(file);
        return IOUtils.toCharArray(fis);
    }

    /**
     * 根据所给路径获取此文件的字节流
     *
     * @param filePath 路径
     * @return 文件的字节流
     * @throws IOException io异常
     */
    public static char[] getFileChar(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given filePath is null!");
            return null;
        }
        File file = new File(filePath);
        return getFileChar(file);
    }

    /**
     * 根据所给文件获取输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws IOException io异常
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory() || !file.canRead()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }

        return new FileInputStream(file);
    }

    /**
     * 根据所给路径获取输入流
     *
     * @param filePath 路径
     * @return 输入流
     * @throws IOException io异常
     */
    public static FileInputStream openInputStream(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given filePath is null!");
            return null;
        }
        File file = new File(filePath);
        return openInputStream(file);
    }

    /**
     * 获取单个文件的大小
     *
     * @param file 要取得文件的大小
     * @return 文件大小，单位字节
     */
    public static Long getFileSize(File file) {
        if (!file.exists() || file.isDirectory()) {
            logger.warn("this file is not exists or it is not a file! file name is '{}'", file.getName());
            return 0L;
        }

        return file.length();
    }

    /**
     * 转换字符串型的文件大小为Long型的(结果是KB)
     *
     * @param size 字符串型的文件大小(单位MB.KB.B,不填默认为MB)
     * @return byte为单位的大小
     */
    public static Long converSizeToKB(String size) {
        Long sizeLong = 0L;
        if (StringUtils.isEmpty(size)) {
            return sizeLong;
        }

        if (NumberUtils.isNumber(size)) {
            return Long.valueOf(size);
        }

        boolean check = false;
        for (String unit : LIST_FILE_SIZE_UNIT) {
            String reg = "[0-9.]{0,}(" + unit + ")$";
            check = size.matches(reg);
            if (check) {
                break;
            }
        }

        if (!check) {
            return sizeLong;
        }

        for (String unit : LIST_FILE_SIZE_UNIT) {
            String number = size.replaceAll(unit, "");
            if (!NumberUtils.isNumber(number)) {
                continue;
            }

            Long d = Long.valueOf(number);
            if ("GB".equals(unit)) {
                sizeLong = d * (1024 * 1024 * 1024);
            } else if ("MB".equals(unit)) {
                sizeLong = d * (1024 * 1024);
            } else if ("KB".equals(unit)) {
                sizeLong = d * 1024;
            } else {
                sizeLong = Long.valueOf(d);
            }
            break;
        }

        return sizeLong;
    }

    /**
     * 获取单个文件的大小
     *
     * @param filePath 要取得文件的path
     * @return 文件大小，单位字节
     */
    public static Long getFileSize(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given file path is null");
            return 0L;
        }

        File file = new File(filePath);

        return getFileSize(file);
    }

    /**
     * 获取文件夹的大小
     *
     * @param file 文件夹
     * @return 文件大小，单位字节
     */
    public static Long getDirectorySize(File file) {
        if (!file.exists()) {
            logger.warn("given file '{}' is null!", file);
            return 0L;
        }
        Long size = 0L;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                return size;
            }

            for (File f : files) {
                if (f.isDirectory()) {
                    size = size + getDirectorySize(f);
                } else {
                    size += getFileSize(f);
                }
            }
        } else {
            size += file.length();
        }

        return size;
    }

    /**
     * 获取文件夹的大小
     *
     * @param filePath 文件夹路径
     * @return 文件夹的大小
     */
    public static Long getDirectorySize(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given file path is null");
            return 0L;
        }
        File file = new File(filePath);

        return getDirectorySize(file);
    }

    /**
     * 遍历指定文件夹下的文件(可以指定扩展名)
     *
     * @param directory 文件夹
     * @param extName   扩展名
     * @return
     */
    public static List<File> getFileUnderDirectory(String directory, String extName) {
        if (StringUtils.isEmpty(directory) || !new File(directory).exists()) {
            logger.error("given no path or not exist!");
            return Collections.emptyList();
        }

        List<File> files = new ArrayList<>();
        File file = new File(directory);
        if (file.isDirectory()) {
            // 是文件夹
            // 获取文件夹下所有的文件或者文件夹
            File[] underFiles = file.listFiles();
            if (ArrayUtils.isEmpty(underFiles)) {
                return files;
            }

            for (File uf : underFiles) {
                if (!uf.isDirectory()) {
                    logger.debug("scan file '{}'!", uf.getName());
                    addFileToList(files, uf, extName);
                } else {
                    files.addAll(getFileUnderDirectory(uf.getAbsolutePath(), extName));
                }
            }
        } else {
            logger.debug("scan file '{}'!", file.getName());
            addFileToList(files, file, extName);
        }

        return files;
    }

    /**
     * 将给定的文件放入list中(按扩展名)
     *
     * @param files   放文件的list
     * @param file    指定的文件
     * @param extName 扩展名
     */
    private static void addFileToList(List<File> files, File file, String extName) {
        if (StringUtils.isEmpty(extName)) {
            files.add(file);
        } else if (StringUtils.isNotEmpty(extName) && file.getName().endsWith(extName)) {
            files.add(file);
        }
    }

    /**
     * Reads the contents of a file into a String.
     *
     * @param filePath the file path to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return contents
     */
    public static String getFileText(String filePath, String encoding) {
        if (StringUtils.isEmpty(filePath)) {
            logger.error("given filePath is null!");
            return StringUtils.EMPTY;
        }

        return getFileText(new File(filePath), encoding);
    }

    /**
     * Reads the contents of a file into a String.
     *
     * @param file     the file to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return contents
     */
    public static String getFileText(File file, String encoding) {
        return getFileText(new FileSystemResource(file), encoding);
    }

    /**
     * Reads the contents of a file into a String.
     *
     * @param resource the resource to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return contents
     */
    public static String getFileText(Resource resource, String encoding) {
        try (InputStream in = resource.getInputStream()) {
            return IOUtils.toString(in, encoding);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * Reads the contents of a file line by line to a List of Strings.
     * The file is always closed.
     *
     * @param file     the file to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return the list of Strings representing each line in the file, never <code>null</code>
     * @throws Exception                            in case of an I/O error
     * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
     * @since Commons IO 1.1
     */
    public static List<String> readLines(File file, String encoding) throws Exception {
        return readLines(openInputStream(file), encoding);
    }

    /**
     * Reads the contents of a file line by line to a List of Strings using the default encoding for the VM.
     * The file is always closed.
     *
     * @param file the file to read, must not be <code>null</code>
     * @return the list of Strings representing each line in the file, never <code>null</code>
     * @throws Exception in case of an I/O error
     * @since Commons IO 1.3
     */
    public static List<String> readLines(File file) throws Exception {
        return readLines(file, null);
    }

    /**
     * Reads the contents of a file line by line to a List of Strings.
     * The file is always closed.
     *
     * @param in       the <code>InputStream</code> to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return the list of Strings representing each line in the file, never <code>null</code>
     * @throws Exception                            in case of an I/O error
     * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
     * @since Commons IO 1.1
     */
    public static List<String> readLines(InputStream in, String encoding) throws Exception {
        if (in == null) {
            return Collections.emptyList();
        }

        return IOUtils.readLines(in, encoding);
    }

    /**
     * Reads the contents of a file line by line to a List of Strings using the default encoding for the VM.
     * The file is always closed.
     *
     * @param in the <code>InputStream</code> to read, must not be <code>null</code>
     * @return the list of Strings representing each line in the file, never <code>null</code>
     * @throws Exception in case of an I/O error
     * @since Commons IO 1.3
     */
    public static List<String> readLines(InputStream in) throws Exception {
        return readLines(in, null);
    }
}
