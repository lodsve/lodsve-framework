package message.mvc.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import message.base.Codeable;

import java.io.IOException;

/**
 * Jackson序列化枚举时，将枚举变成{value: '', name: ''}.<br/>
 * {@link CustomObjectMapper#CustomObjectMapper()}
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/24 下午9:31
 */
public class EnumSerializer extends JsonSerializer<Codeable> {
    @Override
    public void serialize(Codeable value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value instanceof Enum<?>) {
            jgen.writeStartObject();
            jgen.writeStringField("code", value.getCode());
            jgen.writeStringField("title", value.getTitle());
            jgen.writeEndObject();
        }
    }
}
