package com.ikuboo.tools.limiter;

import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.UUID;

public class RateLimiter {
    private final Long rate;
    private final String key;
    private final LimiterType limiterType;
    private final JedisCluster jedisCluster;

    private RateLimiter(Long rate, String key, LimiterType limiterType, JedisCluster jedisCluster) {
        this.rate = rate;
        this.key = key;
        this.limiterType = limiterType;
        this.jedisCluster = jedisCluster;
    }

    public Acquire tryAcquire(){
        String time = LimiterType.CLINET == limiterType ? "5" : "1";
        String replaceKey = LimiterType.CLINET == limiterType ? key + "&" + (System.currentTimeMillis() / 1000) : key;
        ArrayList<Long> eval = (ArrayList)jedisCluster.eval(Script.getScript(), 1, replaceKey, rate.toString(), time);
        return new Acquire(eval.get(0) == 1 ? true : false, eval.get(1));
    }


    public static RateLimiter create(final Long rate, JedisCluster jedisCluster){
        String key = UUID.randomUUID().toString();
        return create(rate, key, LimiterType.CLINET,jedisCluster);
    }

    public static RateLimiter create(final Long rate, String key, JedisCluster jedisCluster){
        return create(rate, key, LimiterType.CLINET,jedisCluster);
    }

    public static RateLimiter create(final Long rate, final String key, final LimiterType limiterType, final JedisCluster jedisCluster){
        if(rate == null || rate < 1){
            throw new IllegalArgumentException("param rate error!");
        }
        if(key == null || key.trim().length() == 0){
            throw new IllegalArgumentException("param key error!");
        }
        if(null == limiterType){
            throw new IllegalArgumentException("param limiterType error!");
        }
        if(null == jedisCluster){
            throw new IllegalArgumentException("param jedisCluster error!");
        }
        return new RateLimiter(rate, key, limiterType, jedisCluster);
    }
}
