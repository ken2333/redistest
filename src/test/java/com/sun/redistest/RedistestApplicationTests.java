package com.sun.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class RedistestApplicationTests {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testSet() {
        this.redisTemplate.opsForValue().set("name1", "java");
        System.out.println(this.redisTemplate.opsForValue().get("study"));
    }

    public void setKey()
    {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("key1", "sun", 10, TimeUnit.SECONDS);

    }

    @Test
    public void test()
    {

        String s = UUID.randomUUID().toString();
        System.out.println(s);

    }


}
