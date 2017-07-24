package lodsve.mvc.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lodsve.core.bean.Codeable;

import java.io.IOException;

/**
 * Jackson序列化枚举时，将枚举变成{value: '', name: ''}.<br/>
 * {@link lodsve.mvc.json.CustomObjectMapper#CustomObjectMapper()}
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/24 下午9:31
 */
public class EnumSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (!(value instanceof Codeable)) {
            return;
        }

        Codeable codeable = (Codeable) value;

        jgen.writeStartObject();
        jgen.writeStringField("code", codeable.getCode());
        jgen.writeStringField("title", codeable.getTitle());
        jgen.writeEndObject();
    }
}
