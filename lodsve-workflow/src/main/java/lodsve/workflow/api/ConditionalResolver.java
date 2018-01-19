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

package lodsve.workflow.api;

import java.util.List;

/**
 * 解析办理人.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午3:57
 */
public interface ConditionalResolver {
    /**
     * 解析办理人
     *
     * @param conditional 表达式
     * @return 办理人
     */
    List<Long> resolveHandlers(String conditional);

    /**
     * 解析办理人的姓名
     *
     * @param id id
     * @return 姓名
     */
    String resolveHandlerName(Long id);
}
