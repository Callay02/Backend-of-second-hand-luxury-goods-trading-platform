package icu.callay.controller;

import cn.dev33.satoken.util.SaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("redis")
public class RedisController {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void StringRedisTemplate(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }

    public RedisController(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @GetMapping("save")
    public SaResult save(@RequestParam("key")String key,@RequestParam("value")String value){
        stringRedisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
        return SaResult.ok();
    }

    @GetMapping("get")
    public SaResult get(@RequestParam("key")String key){
        return SaResult.data(stringRedisTemplate.opsForValue().get("key"));
    }

    @GetMapping("delete")
    public SaResult delete(@RequestParam("key")String key){
        stringRedisTemplate.opsForValue().getAndDelete(key);
        return SaResult.ok();
    }
}
