package com.demo.tx.payment.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SnowflakeIdWorker {

    public static final AtomicLong counter = new AtomicLong(0L);
    /**
     * 北京时间 (2020-01-01)
     */
    private final long twEpoch = 1577865600000L;
    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequenceSerial = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    private AtomicLong sequenceCas = new AtomicLong(0);
    private AtomicLong lastTimestampCas = new AtomicLong(System.currentTimeMillis());
    private Semaphore semaphore = new Semaphore(100);

    //==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextIdSerial() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate userId for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequenceSerial = (sequenceSerial + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequenceSerial == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequenceSerial = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twEpoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequenceSerial;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public long nextIdCASError() {
        try {
            semaphore.acquire();
            long timestamp = timeGen();
            long lastTimestampCAS = lastTimestampCas.get();

            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestampCAS) {
                throw new RuntimeException(
                        String.format("Clock moved backwards.  Refusing to generate userId for %d milliseconds", lastTimestamp - timestamp));
            }

            long sequence = sequenceCas.incrementAndGet();
            if (lastTimestampCAS != timestamp) {
                /**
                 * 1. 当前时间是 5milli，过去时间是 4milli
                 * 2. 线程1 失去 CPU， 等待；线程2 获得 CPU 进行更新，同时获得 sequence 0    =>线程1持续没有获得 CPU
                 * 3. 线程1 还是没有获得 CPU；其他线程更新 sequence 1，此时 userId 为 5milli+1
                 * 4. 时间进入 6milli，过去时间是 5milli，其他线程更新，同时获得 sequence 0
                 * 5. 线程1 获得CPU， 获得 sequence 1， userId 为 5milli+1，重复
                 */
                lastTimestampCas.compareAndSet(lastTimestampCAS, timestamp);
                if (sequenceCas.compareAndSet(sequence, 0))
                    sequence = 0;
                else
                    sequence = sequenceCas.incrementAndGet();
            }

            if ((sequence & sequenceMask) == 0) {
                tilNextMillis(timestamp);
                nextIdCASError();
            }

            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twEpoch) << timestampLeftShift) //
                    | (datacenterId << datacenterIdShift) //
                    | (workerId << workerIdShift) //
                    | sequence;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    private AtomicReference<TimeAndSequence> timeSequenceReference =
            new AtomicReference<>(new TimeAndSequence(System.currentTimeMillis()));

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public long nextIdCAS() {
        /**
         * get TimeAndSequence 必须在 获取当前时间前面，不然会有这种情况发生：
         * 线程1获得当前时间后，失去CPU，线程2获得 CPU，更新 timeSequenceReference，且时间 +1millisecond
         * 这时，线程1出现 当前时间 < 之前存储时间 的情况
         */
        TimeAndSequence timeAndSequence = timeSequenceReference.get();
        long timestamp = timeGen();
        final long lastTimestampCAS = timeAndSequence.getLastTimestamp();

        long sequence = timeAndSequence.incrementAndGetSequence();
        if (lastTimestampCAS != timestamp) {
            final TimeAndSequence newTimeAndSequence = new TimeAndSequence(timestamp);
            if (timeSequenceReference.compareAndSet(timeAndSequence, newTimeAndSequence)) {
                sequence = 1;
            } else {
                timeAndSequence = timeSequenceReference.get();
                timestamp = timeAndSequence.getLastTimestamp();
                sequence = timeAndSequence.incrementAndGetSequence();
            }
        }

        if (sequence > sequenceMask) {
            tilNextMillis(timestamp);

            return nextIdCAS();
        }

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twEpoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    private class TimeAndSequence {
        private final long lastTimestamp;
        private AtomicLong sequence;

        public TimeAndSequence(long lastTimestamp) {
            this.lastTimestamp = lastTimestamp;
            this.sequence = new AtomicLong(1);
        }

        public long getLastTimestamp() {
            return lastTimestamp;
        }

        public long incrementAndGetSequence() {
            return sequence.incrementAndGet();
        }

        @Override
        public String toString() {
            return "TimeAndSequence{" +
                    "lastTimestamp=" + lastTimestamp +
                    ", sequence=" + sequence +
                    '}';
        }
    }

}
