分布式限流
====
### 基于 `Redis` 和 `Lua` 实现的分布式限流功能

### 1.概述
* 限流目前一般都俩种`单机限流`和`分布式限流`<br>
    * 单机限流不损耗性能,一般用Guava类库的RateLimiter即可，但是单机限流每次扩容，缩容线上机器都需要重新计算阈值，还会受负载策略的影响。
    * 分布式限流会损耗一些性能,但是不受线上扩容,缩容，负载策略的影响。
###
 
### 2.使用 
```JAVA

     JedisCluster jedis = new JedisCluster(new HostAndPort("127.0.0.1", 6379));
     //速率为:5次/秒
     RateLimiter rateLimiter = RateLimiter.create(5L,jedis);
     //获取许可
     Acquire acquire = rateLimiter.tryAcquire();
     if(!acquire.isAquire()){
         System.out.println("超过阈值,被限流了......");
         return;
     }

     //normal code ....
 ```
### 3.参数
```JAVA
public static RateLimiter create(Long rate,String key,LimiterType limiterType,JedisCluster jedisCluster)
```
* rate:速率(N次/秒)
* key:限流器的key,通过key隔离不同的限流器,默认为UUID
* limiterType:限流模式,`client` 和 `server`俩种模式,默认为client模式
	* client模式:按照时间戳每秒生成一个key
		* 优点: 1.redis cluster模式可以散列到不同的分片,避免单点,提升性能 2.不会依赖redis key 的过期时间
		* 缺点: 需要保证所有的服务器时间相同
	* server模式:key固定不变
		* 优点: 不依赖服务器时间
		* 缺点: 1.强依赖redis中1秒的过期策略 2:redis cluster模式下所有请求都会打到一个分片上
	* 如果量小用server模式没问题，量大还是建议用client模式
* jedisCluster:redis客户端,如果redis为cluster模式fork项目自己改一下吧。
 

 
 
           
