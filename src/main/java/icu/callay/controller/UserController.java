package icu.callay.controller;



import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.User;
import icu.callay.service.UserService;
import icu.callay.vo.UserRegisterVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;


    /**
     * @param user:
     * @return SaResult
     * @author Callay
     * &#064;description  用户登录
     * &#064;date  2024/4/5 13:23
     */
    @PostMapping("login")
    public SaResult login(@RequestBody User user){
        return userService.userLogin(user);
    }

    /**
     * @param token:
     * @return SaResult
     * @author Callay
     * &#064;description  登出
     * &#064;date  2024/4/5 13:24
     */
    @GetMapping("logout")
    public SaResult logout(@RequestParam(name = "token") String token){
        if(token!=null){
            StpUtil.logoutByTokenValue(token);
            log.info(token+"已注销");
        }
        return SaResult.ok();
    }

    /**
     * @param userRegisterVo:
     * @return SaResult
     * @author Callay
     * &#064;description  用户注册
     * &#064;date  2024/4/5 13:24
     */
    @PostMapping("register")
    public SaResult register(@RequestBody UserRegisterVo userRegisterVo){
        return userService.userRegister(userRegisterVo);
    }

    /**
     * @param user:
     * @return SaResult
     * @author Callay
     * &#064;description  管理员添加用户
     * &#064;date  2024/4/5 13:24
     */
    @PostMapping("adminAddUser")
    @SaCheckRole("管理员")
    public SaResult adminAddUser(@RequestBody User user){
        return userService.adminAddUser(user);
    }

    /**
     * @param email:
     * @param type:
     * @return SaResult
     * @author Callay
     * &#064;description 生成验证码
     * &#064;2024/4/5 17:19
     */
    @GetMapping  ("getcode")
    public SaResult getCode(@RequestParam(name = "email") String email,@RequestParam("type")String type){
        return userService.getCode(email,type);
    }

    /**
     * @return SaResult
     * @author Callay
     * &#064;description  是否登录
     * &#064;date  2024/4/5 13:25
     */
    @GetMapping("isLogin")
    public SaResult isLogin(){
        return SaResult.ok(String.valueOf(StpUtil.isLogin()));
    }

    /**
     * @return SaResult
     * @author Callay
     * &#064;description  根据用户id和密码获取用户信息
     * &#064;date  2024/4/5 13:25
     */
    @GetMapping("getUserInfo")
    public SaResult getUserInfo(){
        return userService.getUserInfo();
    }

    /**
     * @param type:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据类型获取用户信息
     * &#064;2024/4/5 13:27
     */
    @GetMapping("getUserPageByType")
    @SaCheckRole("管理员")
    public SaResult getUserPageByType(@RequestParam("type")int type,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return userService.getUserPageByType(type,page,rows);
    }

    /**
     * @param user:
     * @return SaResult
     * @author Callay
     * &#064;description 删除用户
     * &#064;2024/4/5 13:28
     */
    @PostMapping("deleteUserById")
    @SaCheckRole("管理员")
    public SaResult deleteUserById(@RequestBody User user){
        return userService.deleteUserById(user);
    }

    /**
     * @param user:
     * @return SaResult
     * @author Callay
     * &#064;description 用户修改个人信息
     * &#064;2024/4/5 13:28
     */
    @PostMapping("updateMyUserInfoById")
    public SaResult updateMyUserInfoById(@RequestBody User user){
        return userService.updateMyUserInfoById(user);
    }

    /**
     * @param type:
     * @return SaResult
     * @author Callay
     * &#064;description 根据类型获取用户数量
     * &#064;2024/4/5 13:29
     */
    @GetMapping("adminGetUserNumberByType")
    @SaCheckRole("管理员")
    public SaResult adminGetUserNumberByType(@RequestParam("type")String type){
        return userService.adminGetUserNumberByType(type);
    }

    /**
     * @param user:
     * @return SaResult
     * @author Callay
     * &#064;description 用户重置密码
     * &#064;2024/4/5 17:59
     */
    @PostMapping("userResetPassword")
    public SaResult userResetPassword(@RequestBody UserRegisterVo user){
        return userService.userResetPassword(user);
    }
}

