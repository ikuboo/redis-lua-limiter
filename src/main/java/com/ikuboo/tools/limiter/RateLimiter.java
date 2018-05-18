package com.ikuboo.tools.limiter;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.UUID;

public class RateLimiter {
    private final Long rate;
    private final String key;
    private final LimiterType limiterType;
    private final Jedis jedis;

    private RateLimiter(Long rate, String key, LimiterType limiterType, Jedis jedis) {
        this.rate = rate;
        this.key = key;
        this.limiterType = limiterType;
        this.jedis = jedis;
    }

    public Acquire tryAcquire(){
        String time = LimiterType.CLINET == limiterType ? "5" : "1";
        String replaceKey = LimiterType.CLINET == limiterType ? key + "&" + (System.currentTimeMillis() / 1000) : key;
        ArrayList<Long> eval = (ArrayList)jedis.eval(Script.getScript(), 1, replaceKey, rate.toString(), time);
        return new Acquire(eval.get(0) == 1 ? true : false, eval.get(1));
    }


    public static RateLimiter create(final Long rate, Jedis jedisClient){
        String key = UUID.randomUUID().toString();
        return create(rate, key, LimiterType.CLINET,jedisClient);
    }

    public static RateLimiter create(final Long rate, String key, Jedis jedisClient){
        return create(rate, key, LimiterType.CLINET,jedisClient);
    }

    public static RateLimiter create(final Long rate, final String key, final LimiterType limiterType, final Jedis jedisClient){
        if(rate == null || rate < 1){
            throw new IllegalArgumentException("param rate error!");
        }
        if(key == null || key.trim().length() == 0){
            throw new IllegalArgumentException("param key error!");
        }
        if(null == limiterType){
            throw new IllegalArgumentException("param limiterType error!");
        }
        if(null == jedisClient){
            throw new IllegalArgumentException("param jedisClient error!");
        }
        return new RateLimiter(rate, key, limiterType, jedisClient);
    }
}
