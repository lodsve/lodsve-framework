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

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * C struct body decoder
 * @author Happy Fish / YuQing
 * @version Version 1.17
 */
public abstract class StructBase {
    protected static class FieldInfo {
        protected String name;
        protected int offset;
        protected int size;

        public FieldInfo(String name, int offset, int size) {
            this.name = name;
            this.offset = offset;
            this.size = size;
        }
    }

    /**
     * set fields
     * @param bs byte array
     * @param offset start offset
     */
    public abstract void setFields(byte[] bs, int offset);

    protected String stringValue(byte[] bs, int offset, FieldInfo filedInfo) {
        try {
            return (new String(bs, offset + filedInfo.offset, filedInfo.size, ClientGlobal.g_charset)).trim();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected long longValue(byte[] bs, int offset, FieldInfo filedInfo) {
        return ProtoCommon.buff2long(bs, offset + filedInfo.offset);
    }

    protected int intValue(byte[] bs, int offset, FieldInfo filedInfo) {
        return (int) ProtoCommon.buff2long(bs, offset + filedInfo.offset);
    }

    protected int int32Value(byte[] bs, int offset, FieldInfo filedInfo) {
        return ProtoCommon.buff2int(bs, offset + filedInfo.offset);
    }

    protected byte byteValue(byte[] bs, int offset, FieldInfo filedInfo) {
        return bs[offset + filedInfo.offset];
    }

    protected boolean booleanValue(byte[] bs, int offset, FieldInfo filedInfo) {
        return bs[offset + filedInfo.offset] != 0;
    }

    protected Date dateValue(byte[] bs, int offset, FieldInfo filedInfo) {
        return new Date(ProtoCommon.buff2long(bs, offset + filedInfo.offset) * 1000);
    }
}
