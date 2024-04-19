package icu.callay.controller;



import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.SalespersonUser;
import icu.callay.service.SalespersonUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (SalespersonUser)表控制层
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("salespersonUser")
public class SalespersonUserController {

    private final SalespersonUserService salespersonUserService;


    /**
     * @return SaResult
     * @author Callay
     * &#064;description 销售员根据id获取销售员信息
     * &#064;2024/4/5 14:51
     */
    @GetMapping("getUserInfoById")
    @SaCheckRole("代理商")
    public SaResult getUserInfoById(){
        return salespersonUserService.getUserInfoById();
    }

    /**
     * @param salespersonUser:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id更新用户信息
     * &#064;2024/4/5 14:51
     */
    @PostMapping("updateUserInfoById")
    @SaCheckRole("代理商")
    public SaResult updateUserInfoById(@RequestBody SalespersonUser salespersonUser){
        return salespersonUserService.updateUserInfoById(salespersonUser);
    }

}

