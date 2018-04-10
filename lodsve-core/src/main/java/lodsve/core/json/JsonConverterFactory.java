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

package lodsve.core.json;

/**
 * 获取JsonConverter的工厂.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 15:17
 */
public final class JsonConverterFactory {

    private JsonConverterFactory() {

    }

    /**
     * 获取JsonConverter
     *
     * @param mode json mode
     * @return JsonConverter
     * @see JsonMode
     * @see JsonConverter
     */
    public static JsonConverter getConverter(JsonMode mode) {
        JsonConverter converter;

        switch (mode) {
            case GSON:
                converter = new GsonConverter();
                break;
            case JACKSON:
                converter = new JacksonConverter();
                break;
            case FastJson:
                converter = new FastJsonConverter();
                break;
            default:
                converter = new JacksonConverter();
        }

        return converter;
    }

    /**
     * 使用json的类型
     */
    public enum JsonMode {
        JACKSON, GSON, FastJson
    }
}
