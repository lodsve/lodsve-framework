package message.mvc.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import message.base.Codeable;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 设置Jackson序列化及反序列化时的一些规则.<br/>
 * <ol>
 * <li>设置序列化,反序列化时间时的时区</li>
 * <li>设置序列化枚举项时,将枚举转成<code>{"code":"","title":""}</code></li>
 * <li>设置序列化时间类型时默认的时间格式</li>
 * </ol>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/20 上午5:35
 */
public class CustomObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        //使用默认时区
        setTimeZone(TimeZone.getDefault());

        configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

        // 序列化枚举时的处理
        SimpleModule serializerModule = new SimpleModule();
        serializerModule.addSerializer(Codeable.class, new EnumSerializer());
        registerModule(serializerModule);

        //日期的处理
        //默认是将日期类型转换为yyyy-MM-dd HH:mm
        //如果需要自定义的，则在pojo对象的Date类型上加上注解
        //@com.fasterxml.jackson.annotation.JsonFormat(pattern = "时间格式化")
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    }
}
