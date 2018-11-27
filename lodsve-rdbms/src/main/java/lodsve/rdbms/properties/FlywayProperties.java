/*
 * Copyright (C) 2018, All rights Reserved, Designed By www.xiniaoyun.com
 * @author: 孙昊
 * @date: 2018-11-27 18:37
 * @Copyright: 2018 www.xiniaoyun.com Inc. All rights reserved.
 * 注意：本内容仅限于南京微欧科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package lodsve.rdbms.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@ConfigurationProperties(prefix = "lodsve.flyway", locations = "${params.root}/framework/flyway.properties")
public class FlywayProperties {
    /**
     * flyway的脚本文件所在路径
     */
    private String[] locations = new String[]{"classpath:META-INF/flyway"};

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }
}
