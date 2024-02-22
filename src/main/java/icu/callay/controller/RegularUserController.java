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

    @Autowired
    private RegularUserService regularUserService;

    @GetMapping("getUserInfoById")
    public SaResult getUserInfoById(@RequestParam("id")int id){
        return regularUserService.getUserInfoById(id);
    }

    @PostMapping("updateUserInfoById")
    public SaResult updateUserInfoById(@RequestBody RegularUser regularUser){
        return regularUserService.updateUserInfoById(regularUser);
    }

    //充值
    //接收用户id和充值的金额
    @PostMapping("recharge")
    public SaResult recharge(@RequestBody RegularUser regularUser){
        return regularUserService.recharge(regularUser);
    }

}

