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

package lodsve.cache.annotations;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Cache ImportSelector.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/23 下午3:55
 */
public class CacheImportSelector implements ImportSelector {
    private static final String CACHE_MODE_ATTRIBUTE_NAME = "cache";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCache.class.getName(), importingClassMetadata.getClassName()));

        CacheMode cacheMode = attributes.getEnum(CACHE_MODE_ATTRIBUTE_NAME);

        return new String[]{cacheMode.getCacheConfig().getName()};
    }
}
