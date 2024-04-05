package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.SalespersonUser;
import icu.callay.service.SalespersonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (SalespersonUser)表控制层
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */
@RestController
@RequestMapping("salespersonUser")
public class SalespersonUserController {

    private SalespersonUserService salespersonUserService;
    @Autowired
    public void SalespersonUserService(SalespersonUserService salespersonUserService){
        this.salespersonUserService=salespersonUserService;
    }

    /**
     * @param :
     * @return SaResult
     * @author Callay
     * &#064;description 销售员根据id获取销售员信息
     * &#064;2024/4/5 14:51
     */
    @GetMapping("getUserInfoById")
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
    public SaResult updateUserInfoById(@RequestBody SalespersonUser salespersonUser){
        return salespersonUserService.updateUserInfoById(salespersonUser);
    }

}

