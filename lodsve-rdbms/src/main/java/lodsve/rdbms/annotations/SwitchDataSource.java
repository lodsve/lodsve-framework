/*
 * Copyright (C) 2018, All rights Reserved, Designed By www.xiniaoyun.com
 * @author: 孙昊
 * @date: 2018-11-27 17:08
 * @Copyright: 2018 www.xiniaoyun.com Inc. All rights reserved.
 * 注意：本内容仅限于南京微欧科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package lodsve.rdbms.annotations;

import java.lang.annotation.*;

/**
 * 切换数据源.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchDataSource {
    /**
     * 选择的数据源
     *
     * @return 数据源名称
     */
    String value();
}
