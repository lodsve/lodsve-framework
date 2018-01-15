package lodsve.webservice;

import java.lang.annotation.*;

/**
 * 提供webservice的address.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-15-0015 09:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface AddressProvider {
    /**
     * 提供address
     *
     * @return address
     */
    String value();
}
