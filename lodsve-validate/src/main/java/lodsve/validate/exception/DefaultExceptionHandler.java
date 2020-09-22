/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.validate.exception;

import lodsve.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的异常处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 12-12-2 上午1:11
 */
public class DefaultExceptionHandler extends ExceptionHandler {

    @Override
    public String getMessage(List<ErrorMessage> messages) {
        List<String> msgs = new ArrayList<>(messages.size());
        for (ErrorMessage em : messages) {
            msgs.add(em.getMessage());
        }

        return StringUtils.join(msgs, "\r\n");
    }
}
