package com.sun.redistest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class RedistestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedistestApplication.class, args);
    }


  /*  @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        //定义key序列化方式
        //RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
        //定义value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }*/
  @Bean
  public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      // 1.创建 redisTemplate 模版
      RedisTemplate<Object, Object> template = new RedisTemplate<>();
      // 2.关联 redisConnectionFactory
      template.setConnectionFactory(redisConnectionFactory);
      // 3.创建 序列化类
      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper om = new ObjectMapper();
      // 4.设置可见度
      om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
      // 5.启动默认的类型
      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
      // 6.序列化类，对象映射设置
      jackson2JsonRedisSerializer.setObjectMapper(om);
      // 7.设置 value 的转化格式和 key 的转化格式
      template.setValueSerializer(jackson2JsonRedisSerializer);
      template.setKeySerializer(new StringRedisSerializer());
      template.afterPropertiesSet();
      return template;
  }


    @Bean
    public RedissonClient  redisson()
    {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://203.195.162.66:7100").setPassword("sun19950103");
        RedissonClient redisson = Redisson.create(config);
        return  redisson;
    }



}
