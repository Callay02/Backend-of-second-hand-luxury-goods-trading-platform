package icu.callay.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import icu.callay.entity.RegularUser;
import icu.callay.mapper.RegularUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.controller
 * &#064;className:      TestController
 * &#064;author:     Callay
 * &#064;description:  TODO
 * &#064;date:    2024/4/6 11:51
 * &#064;version:    1.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    private RegularUserMapper regularUserMapper;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void StringRedisTemplate(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }
    @Autowired
    public void RegularUserMapper(RegularUserMapper regularUserMapper){
        this.regularUserMapper=regularUserMapper;
    }

    @GetMapping("rollback")
    @Transactional(rollbackFor = Exception.class)
    public SaResult rollback(){
        try {
            regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id","1422602267").set("money",1000));
            RegularUser regularUser = regularUserMapper.selectById("hhhhhh");
            regularUserMapper.update(regularUser,new UpdateWrapper<RegularUser>().eq("id","hhhh"));
            return SaResult.data(regularUser);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("save")
    public SaResult save(@RequestParam("key")String key, @RequestParam("value")String value){
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
