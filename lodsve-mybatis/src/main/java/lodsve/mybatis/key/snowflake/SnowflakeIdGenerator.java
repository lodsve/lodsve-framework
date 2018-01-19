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

package lodsve.mybatis.key.snowflake;

import lodsve.mybatis.key.IDGenerator;

/**
 * twitter的snowflake的ID生成器实现
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-29 下午23:39
 */
public class SnowflakeIdGenerator implements IDGenerator {
    private final static long TWEPOCH = 1361753741828L;
    private long sequence = 0L;
    private final static long WORKER_ID_BITS = 4L;
    private final static long SEQUENCE_BITS = 10L;

    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long SEQUENCE_MASK = -1L ^ -1L << SEQUENCE_BITS;

    private long lastTimestamp = -1L;

    @Override
    public synchronized Long nextId(String sequenceName) {
        long timestamp = this.timeGen();

        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;

        long workerId = 1L;
        return ((timestamp - TWEPOCH << TIMESTAMP_LEFT_SHIFT)) | (workerId << WORKER_ID_SHIFT) | (this.sequence);
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();

        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
