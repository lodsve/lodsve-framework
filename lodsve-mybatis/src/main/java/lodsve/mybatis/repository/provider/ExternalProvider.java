/*
 * Copyright (C) 2018, All rights Reserved, Designed By www.xiniaoyun.com
 * @author: 孙昊
 * @date: 2018-10-30 18:00
 * @Copyright: 2018 www.xiniaoyun.com Inc. All rights reserved.
 * 注意：本内容仅限于南京微欧科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package lodsve.mybatis.repository.provider;

import java.util.List;

/**
 * 基础crud provider.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public interface ExternalProvider {

    /**
     * 返回外部系统自定义的base dao
     *
     * @return 外部系统自定义的base dao
     */
    List<Class<?>> provider();
}
