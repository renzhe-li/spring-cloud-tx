package com.demo.tx.payment.service.impl;

import org.springframework.data.redis.core.script.RedisScript;

public interface RedisScriptContainer {

    RedisScript<String> getRedisScript(String luaFilename);

}
