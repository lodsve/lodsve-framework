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

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.vip.vjtools.vjkit.collection.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义一个BeanMapper用来做各种O的转换.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class BeanMapper {
    private final static Mapper MAPPER;

    static {
        MAPPER = DozerBeanMapperBuilder.buildDefault();
    }

    /**
     * 简单的复制出新类型对象.
     *
     * @param source           源数据
     * @param destinationClass 目标类型
     * @param <S>              源类型
     * @param <D>              目标类型
     * @return 目标类型对象
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return MAPPER.map(source, destinationClass);
    }

    /**
     * 简单的复制出新类型对象.
     *
     * @param source      源数据
     * @param destination 目标
     */
    public static void map(Object source, Object destination) {
        MAPPER.map(source, destination);
    }

    /**
     * 简单的复制出新对象ArrayList
     *
     * @param sourceList       源数据List
     * @param destinationClass 目标List中的类型
     * @param <S>              源类型
     * @param <D>              目标类型
     * @return 包裹着目标类型的List
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            if (source != null) {
                destinationList.add(MAPPER.map(source, destinationClass));
            }
        }
        return destinationList;
    }

    /**
     * 简单复制出新对象数组
     *
     * @param sourceArray      源数据Array
     * @param destinationClass 目标Array中的类型
     * @param <S>              源类型
     * @param <D>              目标类型
     * @return 包裹着目标类型的Array
     */
    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);

        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = MAPPER.map(sourceArray[i], destinationClass);
                i++;
            }
        }

        return destinationArray;
    }
}
