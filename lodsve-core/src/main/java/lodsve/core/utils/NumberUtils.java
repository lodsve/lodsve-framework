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
