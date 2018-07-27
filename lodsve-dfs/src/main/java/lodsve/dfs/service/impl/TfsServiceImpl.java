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

package lodsve.dfs.service.impl;

import lodsve.dfs.service.DfsService;

import java.io.File;

/**
 * tfs.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-4-0004 13:20
 */
public class TfsServiceImpl implements DfsService {
    @Override
    public String upload(byte[] fileBytes, String suffix) {
        return null;
    }

    @Override
    public String upload(File file) {
        return null;
    }

    @Override
    public String getUrl(String filePath) {
        return null;
    }

    @Override
    public byte[] download(String filePath) {
        return new byte[0];
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }
}
