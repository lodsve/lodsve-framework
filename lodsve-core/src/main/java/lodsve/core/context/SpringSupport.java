/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.core.context;

import lodsve.core.utils.StringUtils;
import org.springframework.util.Assert;

/**
 * 所有main方法继承此类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/20 上午10:17
 */
public class SpringSupport {
    protected SpringSupport() {
        Assert.isTrue(!(StringUtils.isBlank(supportConfigLocation()) && null == supportJavaConfig()));

        if (null != supportJavaConfig()) {
            new SpringContext.Builder().config(supportJavaConfig()).bean(this).build();
        } else {
            new SpringContext.Builder().config(supportConfigLocation()).bean(this).build();
        }
    }

    /**
     * 重写此方法，提供spring配置文件的路径，否则使用默认的配置文件
     *
     * @return spring配置文件的路径
     */
    public String supportConfigLocation() {
        return "";
    }

    /**
     * 重写此方法，提供spring JavaConfig配置Class，否则抛出异常
     *
     * @return spring JavaConfig配置Class
     */
    public Class<?> supportJavaConfig() {
        return null;
    }
}
