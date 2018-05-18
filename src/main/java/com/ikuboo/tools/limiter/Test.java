package com.ikuboo.tools.limiter;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Test {
    public static void main(String[] args) {
        final String script = ScriptUtil.getScript("limit.lua");
        final Jedis jedis = new Jedis("ikuboo1", 6379);

        AtomicLong luaLimiterCount = new AtomicLong();
        AtomicLong javaLimiterCount = new AtomicLong();

        Long begin = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            boolean limiter = limiterLua(script, jedis, 100);
            //boolean limiter = limiterJava(jedis, 100);
            if (!limiter){
                luaLimiterCount.incrementAndGet();
            }
            /*if(!javalimiter){
                javaLimiterCount.incrementAndGet();
            }*/
        }

        System.out.println("lua:" + luaLimiterCount.get());
        System.out.println("java:" + javaLimiterCount.get());
        System.out.println("time:" + (System.currentTimeMillis() - begin));
    }

    public static boolean limiterLua(String script, Jedis jedis, Integer qps){
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        List<String> keyList = Collections.singletonList(key);
        List<String> limitList = Collections.singletonList(String.valueOf(qps));

        Object eval = jedis.eval(script, keyList, limitList);

        if (-1 == (Long) eval) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean limiterJava(Jedis jedis, Integer qps) {
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        Long incr = jedis.incr(key);

        if (incr == 1){
            jedis.expire(key, 1);
        }

        if (incr > qps){
            return false;
        }else {
            return true;
        }
    }

}
