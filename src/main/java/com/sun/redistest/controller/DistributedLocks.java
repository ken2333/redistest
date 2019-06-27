package com.sun.redistest.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ken
 * @date 2019/6/27  21:47
 * @description 分布式锁
 */
@RestController("lock")
public class DistributedLocks {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * 首先生产一个UUID，这个UUID是确保在删除key的时候，是同一个，避免了删除了其他线程创建UUID。
     * 这个是为了避免遇到这种情况，当redis主服务器设置好key后，还没有同步到从服务器的时候，主服务器突然
     * 宕机了，这时候，从服务器会升为主服务器。其他的线程在获取锁的时候，由于缺失了之前的Key，所以可以
     * 获取分布式锁，这样有可能在之前的线程在跑完后，会删除其他线程的锁。
     */
    @RequestMapping("getlock")
    public String getLock(String key) {
        JSONObject ob = new JSONObject();
        String uuid=null;
        try {
            uuid = UUID.randomUUID().toString();
            //设置一个key-value，过期的时间是10s
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, uuid, 10, TimeUnit.SECONDS);
            if (!result) {
                ob.put("success", false);
                ob.put("msg", "获取锁失败！");
                return ob.toJSONString();
            }
            System.out.println("开始执行业务");
            redisTemplate.opsForValue().increment("run", 1);
            System.out.println("业务执行结束");
        } finally {
            String currentUUID = redisTemplate.opsForValue().get(key);
            if (currentUUID!=null&&uuid.equals(currentUUID)) {
                //只有和当前的uuid相同才删除当前的锁
                redisTemplate.delete(key);
            }
        }
        ob.put("success", true);
        ob.put("msg", "获取锁成功!");
        return ob.toJSONString();
    }
}
