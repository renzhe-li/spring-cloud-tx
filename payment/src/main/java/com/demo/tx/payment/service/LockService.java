package com.demo.tx.payment.service;

public interface LockService {

    /**
     * Key: LOCK_${@param key}
     * Value: instanceId:ThreadName:reentryTimes
     * Default Expire Secs: 10s
     *
     * @param key
     * @return
     */
    boolean lock(String key);

    /**
     * Key: LOCK_${@param key}
     * Value: instanceId:ThreadName:reentryTimes
     * Expire Milli Seconds: ${@param expireMilliSecs}
     *
     * @param key
     * @param expireMilliSecs
     * @return
     */
    boolean lock(String key, long expireMilliSecs);

    /**
     * Key: LOCK_${@param key}
     * IF Thread.currentThread() is the lock owner, then: Value: instanceId:ThreadName:${reentryTimes-1}
     * Else do nothing
     *
     * @param key
     * @return
     */
    boolean unlock(String key);

}
