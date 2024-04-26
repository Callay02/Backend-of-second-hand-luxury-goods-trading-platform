package icu.callay.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import icu.callay.util.StartupTimeListener;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.controller
 * &#064;className:      TestController
 * &#064;author:     Callay
 * &#064;description:  测试控制器
 * &#064;date:    2024/4/6 11:51
 * &#064;version:    1.0
 */
@RestController
@RequestMapping("system")
@RequiredArgsConstructor
@SaCheckRole("管理员")
public class SystemController {

    private final StartupTimeListener startupTimeListener;

    /**
     * @return SaResult
     * @author Callay
     * &#064;description 获取系统时间
     * &#064;2024/4/26 23:50
     */
    @GetMapping("getRuntime")
    public SaResult getRuntime(){

        return SaResult.data(System.currentTimeMillis()-startupTimeListener.getStartupTime());
    }
}
