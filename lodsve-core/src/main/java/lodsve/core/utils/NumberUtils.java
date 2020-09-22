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
package lodsve.core.utils;

/**
 * 对于数字操作类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2012-4-28 上午10:36:12
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    /**
     * 私有化构造器
     */
    private NumberUtils() {
    }

    /**
     * 计算两个数的最大公约数
     *
     * @param firstNum  数字1
     * @param secondNum 数字2
     * @return 最大公约数
     */
    public static int getMaxCommonDivisor(int firstNum, int secondNum) {
        int commonDivisor = 0;

        while (secondNum != 0) {
            commonDivisor = firstNum % secondNum;
            firstNum = secondNum;
            secondNum = commonDivisor;
        }

        return commonDivisor;
    }

    /**
     * 对一个分数约分
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return new int[]{分子, 分母}
     */
    public static int[] reduction(int numerator, int denominator) {
        int commonDivisor = getMaxCommonDivisor(numerator, denominator);

        return new int[]{numerator / commonDivisor, denominator / commonDivisor};
    }

    /**
     * 16进制数字转换成10进制
     *
     * @param sixteenHex 16进制数字
     * @return 10进制
     */
    public static Integer hex16To10(String sixteenHex) {
        if (StringUtils.isEmpty(sixteenHex)) {
            return 0;
        }

        return Integer.valueOf(sixteenHex, 16);
    }

}
