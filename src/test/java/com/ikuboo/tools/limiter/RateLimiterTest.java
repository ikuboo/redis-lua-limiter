package com.ikuboo.tools.limiter;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RateLimiterTest {

    @Test
    public void tryAcquire() {

        Jedis jedis = new Jedis("ikuboo1", 6379);
        //速率为:5次/秒
        RateLimiter rateLimiter = RateLimiter.create(5L,jedis);
        //获取许可
        Acquire acquire = rateLimiter.tryAcquire();
        if(!acquire.isAquire()){
            System.out.println("超过阈值,被限流了");
            return;
        }

        //normal code
    }
}
