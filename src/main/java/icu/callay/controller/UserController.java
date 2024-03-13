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
 * @author Callay
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
    public SaResult logout(@RequestParam(name = "token") String token){
        if(token!=null){
            StpUtil.logoutByTokenValue(token);
            //System.out.println(token);
            log.info(token+"已注销");
        }
        return SaResult.ok();
    }

    //注册
    @PostMapping("register")
    public SaResult register(@RequestBody User user){
        //System.out.println(user);
        return userService.userRegister(user);
    }

    @GetMapping  ("getcode")
    public SaResult getCode(@RequestParam(name = "email") String email){
        return userService.getCode(email);
    }

    @GetMapping("isLogin")
    public SaResult isLogin(){
        return SaResult.ok(String.valueOf(StpUtil.isLogin()));
    }

    @GetMapping("getUserInfo")
    public SaResult getUserInfo(@RequestParam(name = "id") Long id,@RequestParam("password")String pwd){
        return userService.getUserInfo(id,pwd);
    }

    @GetMapping("getUserPageByType")
    public SaResult getUserPageByType(@RequestParam("type")int type,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return userService.getUserPageByType(type,page,rows);
    }

    @PostMapping("deleteUserById")
    public SaResult deleteUserById(@RequestBody User user){
        return userService.deleteUserById(user);
    }

    //用户修改个人信息
    @PostMapping("updateMyUserInfoById")
    public SaResult updateMyUserInfoById(@RequestBody User user){
        return userService.updateMyUserInfoById(user);
    }
}

