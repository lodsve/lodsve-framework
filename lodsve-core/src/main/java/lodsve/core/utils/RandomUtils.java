package lodsve.core.utils;

import org.apache.commons.lang.ArrayUtils;

import java.util.Random;

/**
 * 随机生成数字或者字符串的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-4-24 下午08:25:57
 */
public class RandomUtils {
    private static final String LETTER_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTER_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT = "0123456789";

    /**
     * 私有化构造器
     */
    private RandomUtils() {
    }

    /**
     * 获取长度为length的随机数
     *
     * @param length 长度
     * @return 获取的随机数(String)
     */
    public static String getRandomNum(int length) {
        if (length < 1) {
            return StringUtils.EMPTY;
        }

        long returnRandom = 0;
        long min = 1;
        long max = 1;

        for (int i = 0; i < length - 1; i++) {
            min *= 10;
        }
        for (int j = 0; j < length; j++) {
            max *= 10;
        }

        while (returnRandom <= min) {
            returnRandom = (long) (Math.random() * max);
        }

        return Long.toString(returnRandom);
    }

    /**
     * 生成随机数字
     *
     * @param length 生成的密码的总长度
     * @return 数字的字符串
     */
    public static String randomNum(int length) {
        if (length < 1) {
            return StringUtils.EMPTY;
        }
        // 10个数字
        final int maxNum = 10;
        char[] str = DIGIT.toCharArray();

        StringBuffer tempString = new StringBuffer(100);
        generateRandomNumberString(tempString, length, maxNum, str);

        return tempString.toString();
    }

    /**
     * 生成随机字符串
     *
     * @param length 生成的密码的总长度
     * @return 字符串(大小写字母+数字)
     */
    public static String randomString(int length, boolean lower, boolean upper, boolean digit) {
        if (length < 1) {
            return StringUtils.EMPTY;
        }
        //26个字母*2 + 10个数字
        final int maxNum = 62;
        char[] str = {};

        if (lower) str = ArrayUtils.addAll(str, LETTER_LOWER.toCharArray());
        if (upper) str = ArrayUtils.addAll(str, LETTER_UPPER.toCharArray());
        if (digit) str = ArrayUtils.addAll(str, DIGIT.toCharArray());

        StringBuffer tempString = new StringBuffer(100);
        generateRandomNumberString(tempString, length, maxNum, str);

        return tempString.toString();
    }

    /**
     * 生成随机字符串
     *
     * @param length 生成的密码的总长度
     * @return 字符串(大小写字母+数字)
     */
    public static synchronized String randomString(int length) {
        if (length < 1) {
            return StringUtils.EMPTY;
        }

        Random randGen = new Random();
        char[] numbersAndLetters = ArrayUtils.addAll(null, DIGIT.toCharArray());
        numbersAndLetters = ArrayUtils.addAll(numbersAndLetters, LETTER_LOWER.toCharArray());
        numbersAndLetters = ArrayUtils.addAll(numbersAndLetters, DIGIT.toCharArray());
        numbersAndLetters = ArrayUtils.addAll(numbersAndLetters, LETTER_UPPER.toCharArray());

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(72)];
        }
        return new String(randBuffer);
    }

    private static void generateRandomNumberString(StringBuffer tempString, int length, int maxNum, char[] str) {
        Random random = new Random();
        int i, count = 0;
        while (count < length) {
            /**
             * 生成随机数，取绝对值，防止生成负数
             *
             * Random.nextInt(int num)	返回一个在[0, n)
             * 所以不会造成数组下标越界的情况
             */
            i = Math.abs(random.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                tempString.append(str[i]);
                count++;
            }
        }
    }
}
