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

package lodsve.dfs.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.dfs.enums.DfsType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用fs.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-4-0004 10:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableLodsve
@Import(DfsConfigurationSelector.class)
public @interface EnableDfs {

    /**
     * 指定使用的文件系统类型{@link lodsve.dfs.enums.DfsType}，默认为FAST_DFS
     *
     * @return 类型
     */
    DfsType value() default DfsType.FAST_DFS;
}
