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

package lodsve.web.mvc.convert;

import lodsve.core.utils.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 时间类型转换器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-18 15:28
 */
public class StringDateConvertFactory implements ConverterFactory<String, Date>, ConditionalConverter {
    /**
     * yyyy-MM-dd hh:mm
     */
    private static final Pattern PATTERN_ONE = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2} (\\d){1,2}[:](\\d){1,2}");
    /**
     * yyyy-MM-dd
     */
    private static final Pattern PATTERN_TWO = Pattern.compile("(\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");
    /**
     * hh:mm yyyy-MM-dd
     */
    private static final Pattern PATTERN_THREE = Pattern.compile("(\\d){1,2}[:](\\d){1,2} (\\d){2,4}[-](\\d){1,2}[-](\\d){1,2}");


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<?> clazz = targetType.getType();
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Date> Converter<String, T> getConverter(Class<T> targetType) {
        return new String2Date();
    }

    private class String2Date<T extends Date> implements Converter<String, Date> {
        @Override
        public Date convert(String source) {
            try {
                String pattern = getDateFormatPattern(source);
                DateFormat df = new SimpleDateFormat(pattern);

                return df.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * get date format pattern for what parameter in request
         *
         * @param source
         * @return
         */
        private String getDateFormatPattern(String source) {
            if (StringUtils.isEmpty(source)) {
                return "yyyy-MM-dd HH:mm";
            }

            if (PATTERN_ONE.matcher(source).matches()) {
                return "yyyy-MM-dd HH:mm";
            }
            if (PATTERN_TWO.matcher(source).matches()) {
                return "yyyy-MM-dd";
            }
            if (PATTERN_THREE.matcher(source).matches()) {
                return "HH:mm yyyy-MM-dd";
            }

            return "yyyy-MM-dd HH:mm";
        }
    }
}
