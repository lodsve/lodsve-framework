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

package lodsve.validate.exception;

import lodsve.validate.constants.ValidateConstants;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 异常处理类,如果要实现自定义异常,则全部继承这个类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-12-2 上午1:08
 */
public abstract class ExceptionHandler {

    /**
     * 处理异常的方法
     *
     * @param messages 检验结果
     * @throws Exception
     */
    public final void doHandleException(List<ErrorMessage> messages) throws IllegalValidateException {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }

        throw new IllegalValidateException(ValidateConstants.getMessage("error-occurred") + "\r\n" + getMessage(messages));
    }

    public abstract String getMessage(List<ErrorMessage> messages);
}
