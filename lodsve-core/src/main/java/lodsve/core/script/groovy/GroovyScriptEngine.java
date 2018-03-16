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

package lodsve.core.script.groovy;

import lodsve.core.script.AbstractScriptEngine;

/**
 * groovy.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/9 上午11:16
 */
public class GroovyScriptEngine extends AbstractScriptEngine {
    @Override
    protected String getScriptName() {
        return "groovy";
    }
}
