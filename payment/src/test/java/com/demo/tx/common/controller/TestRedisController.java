package com.demo.tx.common.controller;

import com.demo.tx.payment.controller.advice.ResponseResult;
import com.demo.tx.payment.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ResponseResult
@RestController
@RequestMapping("locks")
public class TestRedisController {

    @Autowired
    private LockService lockService;

    @PostMapping
    public boolean lock(String key, long expire) {
        log.info("Thread: {}", Thread.currentThread().getId());

        return lockService.lock(key, expire * 1000);
    }

    @DeleteMapping
    public boolean unLock(String key) {
        log.info("Thread: {}", Thread.currentThread().getId());

        return lockService.unlock(key);
    }

}
