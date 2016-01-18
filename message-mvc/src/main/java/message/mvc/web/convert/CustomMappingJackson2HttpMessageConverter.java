package message.mvc.web.convert;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;

/**
 * 自动将domain类转成dto类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/9 下午3:40
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (object instanceof Page) {
            // 分页的
            object = converterForPage((Page) object);
        } else if (object instanceof Collection) {
            // 返回集合
            object = converterForCollection((Collection) object);
        } else {
            object = converterForDomain(object);
        }

        super.writeInternal(object, outputMessage);
    }

    private Object converterForPage(Page object) {
        // 分页的
        if (CollectionUtils.isEmpty(object.getContent())) {
            return object;
        }

        Class<?> p0 = object.getContent().get(0).getClass();
        AbstractConverter converter = ConverterHelper.getConverter(p0);

        if (converter == null) {
            return object;
        }

        return object.map(converter);
    }

    private Object converterForCollection(Collection object) {
        // 分页的
        if (CollectionUtils.isEmpty(object)) {
            return object;
        }

        Class<?> p0 = object.toArray()[0].getClass();
        AbstractConverter converter = ConverterHelper.getConverter(p0);

        if (converter == null) {
            return object;
        }

        return converter.convert(object);
    }

    private Object converterForDomain(Object object) {
        AbstractConverter converter = ConverterHelper.getConverter(object.getClass());
        if (converter == null) {
            return object;
        }

        return converter.convert(object);
    }
}
