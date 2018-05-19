package com.ikuboo.tools.limiter;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RateLimiterTest {

    @Test
    public void tryAcquire() {
        JedisCluster jedisCluster= new JedisCluster(new HostAndPort("ikuboo1", 6379));
        //速率为:5次/秒
        RateLimiter rateLimiter = RateLimiter.create(5L,jedisCluster);
        //获取许可

        Acquire acquire = rateLimiter.tryAcquire();
        if(!acquire.isAquire()){
            System.out.println("超过阈值,被限流了");
        }

        //normal code
    }
}
