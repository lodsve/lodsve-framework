package lodsve.mvc.convert;

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
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-18 15:28
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
