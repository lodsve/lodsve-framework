package lodsve.mybatis.key.snowflake;

import lodsve.mybatis.key.IDGenerator;

/**
 * twitter的snowflake的ID生成器实现
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-29 下午23:39
 */
public class SnowflakeIdGenerator implements IDGenerator {
    private final static long twepoch = 1361753741828L;
    private long sequence = 0L;
    private final static long workerIdBits = 4L;
    private final static long sequenceBits = 10L;

    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    private final static long sequenceMask = -1L ^ -1L << sequenceBits;

    private long lastTimestamp = -1L;

    @Override
    public synchronized Long nextId(String sequenceName) {
        long timestamp = this.timeGen();

        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & sequenceMask;
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
        return ((timestamp - twepoch << timestampLeftShift)) | (workerId << workerIdShift) | (this.sequence);
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
