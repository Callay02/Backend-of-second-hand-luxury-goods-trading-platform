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

    @Autowired
    private SalespersonUserService salespersonUserService;

    //销售员根据id获取销售员信息
    @GetMapping("getUserInfoById")
    public SaResult getUserInfoById(){
        return salespersonUserService.getUserInfoById();
    }

    @PostMapping("updateUserInfoById")
    public SaResult updateUserInfoById(@RequestBody SalespersonUser salespersonUser){
        return salespersonUserService.updateUserInfoById(salespersonUser);
    }

}

