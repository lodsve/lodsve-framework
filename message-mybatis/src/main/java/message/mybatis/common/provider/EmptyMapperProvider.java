package message.mybatis.common.provider;

import message.mybatis.common.mapper.MapperHelper;
import message.mybatis.common.mapper.MapperTemplate;

/**
 * 空方法Mapper接口默认MapperTemplate<br/>
 * 如BaseSelectMapper，接口纯继承，不包含任何方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 */
public class EmptyMapperProvider extends MapperTemplate {

    public EmptyMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public boolean supportMethod(String msId) {
        return false;
    }
}
