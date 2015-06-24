package message.mvc.convert;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import message.base.convert.ConvertGetter;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 使用默认时区.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/20 上午5:35
 */
public class CustomObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        //使用默认时区
        BaseSettings baseSettings = new BaseSettings(DEFAULT_INTROSPECTOR,
                DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(),
                null, null, null,
                Locale.getDefault(),
                TimeZone.getDefault(),
                Base64Variants.getDefaultVariant() // 2.1
        );

        _serializationConfig = new SerializationConfig(baseSettings, _subtypeResolver, _mixInAnnotations);
        _deserializationConfig = new DeserializationConfig(baseSettings, _subtypeResolver, _mixInAnnotations);
        configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

        // 序列化枚举时的处理
        SimpleModule module = new SimpleModule();
        module.addSerializer(ConvertGetter.class, new EnumSerializer());
        registerModule(module);
    }
}
