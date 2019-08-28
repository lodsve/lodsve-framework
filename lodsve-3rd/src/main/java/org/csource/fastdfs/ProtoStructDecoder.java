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

package org.csource.fastdfs;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * C struct body decoder
 * @author Happy Fish / YuQing
 * @version Version 1.17
 */
public class ProtoStructDecoder<T extends StructBase> {
    /**
     * Constructor
     */
    public ProtoStructDecoder() {
    }

    /**
     * decode byte buffer
     */
    public T[] decode(byte[] bs, Class<T> clazz, int fieldsTotalSize) throws Exception {
        if (bs.length % fieldsTotalSize != 0) {
            throw new IOException("byte array length: " + bs.length + " is invalid!");
        }

        int count = bs.length / fieldsTotalSize;
        int offset;
        T[] results = (T[]) Array.newInstance(clazz, count);

        offset = 0;
        for (int i = 0; i < results.length; i++) {
            results[i] = clazz.newInstance();
            results[i].setFields(bs, offset);
            offset += fieldsTotalSize;
        }

        return results;
    }
}
