package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.service.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (RegularUser)表控制层
 *
 * @author makejava
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

}

