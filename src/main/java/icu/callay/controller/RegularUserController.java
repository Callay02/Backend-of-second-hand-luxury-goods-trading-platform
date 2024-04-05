package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.RegularUser;
import icu.callay.service.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (RegularUser)表控制层
 *
 * @author Callay
 * @since 2024-02-07 19:45:58
 */
@RestController
@RequestMapping("regularUser")
public class RegularUserController {

    private RegularUserService regularUserService;
    @Autowired
    public void RegularUserService(RegularUserService regularUserService){
        this.regularUserService=regularUserService;
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id获取用户信息
     * &#064;2024/4/5 14:39
     */
    @GetMapping("getUserInfoById")
    public SaResult getUserInfoById(@RequestParam("id")int id){
        return regularUserService.getUserInfoById(id);
    }

    /**
     * @param regularUser:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id更新用户信息
     * &#064;2024/4/5 14:40
     */
    @PostMapping("updateUserInfoById")
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
    public SaResult recharge(@RequestBody RegularUser regularUser){
        return regularUserService.recharge(regularUser);
    }

}

