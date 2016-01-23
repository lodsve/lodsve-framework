package message.mvc.web.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * domain-->dto转换器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/9 下午3:47
 */
public abstract class AbstractConverter<S, T> implements Converter<S, T> {

    @Override
    public T convert(S source) {
        if (source == null) {
            return null;
        }
        return internalConvert(source);
    }

    protected abstract T internalConvert(S source);

    public List<T> convert(Collection<S> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(sources.size());
        for (S source : sources) {
            result.add(convert(source));
        }
        return result;
    }
}
