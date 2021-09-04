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
package lodsve.wechat.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 用户性别枚举.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/2 下午4:27
 */
public enum Sex {
    MAN, WOMAN, UNKNOWN;

    @JsonCreator
    public static Sex eval(int index) {
        switch (index) {
            case 1:
                return MAN;
            case 2:
                return WOMAN;
            case 0:
                return UNKNOWN;
            default:
                break;
        }

        return null;
    }
}
