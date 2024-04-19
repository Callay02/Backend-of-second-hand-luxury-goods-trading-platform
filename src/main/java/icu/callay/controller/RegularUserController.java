package icu.callay.controller;



import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.RegularUser;
import icu.callay.service.RegularUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (RegularUser)表控制层
 *
 * @author Callay
 * @since 2024-02-07 19:45:58
 */
@RestController
@RequestMapping("regularUser")
@RequiredArgsConstructor
public class RegularUserController {

    private final RegularUserService regularUserService;


    /**
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id获取用户信息
     * &#064;2024/4/5 14:39
     */
    @GetMapping("getUserInfoById")
    @SaCheckRole("普通用户")
    public SaResult getUserInfoById(){
        return regularUserService.getUserInfoById();
    }

    /**
     * @param regularUser:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id更新用户信息
     * &#064;2024/4/5 14:40
     */
    @PostMapping("updateUserInfoById")
    @SaCheckRole("普通用户")
    public SaResult updateUserInfoById(@RequestBody RegularUser regularUser){
        return regularUserService.updateUserInfoById(regularUser);
    }

    /**
     * @param regularUser:
     * @return SaResult
     * @author Callay
     * &#064;description 充值(接收用户id和充值的金额)
     * &#064;2024/4/5 14:40
     */
    @PostMapping("recharge")
    @SaCheckRole("管理员")
    public SaResult recharge(@RequestBody RegularUser regularUser){
        return regularUserService.recharge(regularUser);
    }

}

