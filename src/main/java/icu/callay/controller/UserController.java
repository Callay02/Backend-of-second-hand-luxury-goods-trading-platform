package icu.callay.controller;



import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.User;
import icu.callay.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2024-01-11 23:24:06
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }


    //登录
    @PostMapping("login")
    public SaResult login(@RequestBody User user){
        return userService.userLogin(user);
    }

    @GetMapping("logout")
    public SaResult logout(@RequestParam(name = "id") String id){
        StpUtil.logout(id);
        return SaResult.ok();
    }

    //注册
    @PostMapping("register")
    public SaResult register(@RequestBody User user){
        return userService.userRegister(user);
    }

    @GetMapping  ("getcode")
    public SaResult getCode(@RequestParam(name = "email") String email){
        return userService.getCode(email);
    }
}

