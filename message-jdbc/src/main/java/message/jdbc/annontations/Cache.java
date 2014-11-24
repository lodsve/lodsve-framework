package message.jdbc.annontations;

import java.lang.annotation.*;

/**
 * 标识Entity是否存入缓存.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-23 下午10:29
 */
@Inherited                  //此注解可以被继承
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    /**
     * 缓存中存在时间(默认31天)
     *
     * @return
     */
    public long expire() default 31 * 24 * 3600;

    /**
     * cache存在的范围(默认是ENTITY_REGION)
     *
     * @return
     */
    public String cacheRegion() default "ENTITY_REGION";
}
