/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.rdbms.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * Pool Setting.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 14:28
 */
@Setter
@Getter
public class PoolSetting {
    private String driverClassName = "com.mysql.jdbc.Driver";
    @Required
    private String url;
    @Required
    private String username;
    @Required
    private String password;
    private Integer initialSize = 10;
    private Integer maxActive = 100;
    private Integer minIdle = 20;
    private Integer maxWait = 60000;
    private Boolean removeAbandoned = true;
    private Integer removeAbandonedTimeout = 180;
    private Boolean testOnBorrow = true;
    private Boolean testOnReturn = true;
    private Boolean testWhileIdle = false;
    private String validationQuery = "select 1";
    private Integer maxIdle = 5;
}
