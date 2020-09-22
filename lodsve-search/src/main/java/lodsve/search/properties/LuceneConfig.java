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
package lodsve.search.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Lucene Config.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 14:47
 */
@Setter
@Getter
public class LuceneConfig {
    /**
     * 高亮前缀
     */
    private String prefix = "<span style='color: red'>";
    /**
     * 高亮后缀
     */
    private String suffix = "</span>";
    /**
     * lucene索引文件路径
     */
    private String index;
    /**
     * 分词器
     */
    private Class<?> analyzer = StandardAnalyzer.class;
}
