package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.SalespersonUser;

/**
 * (SalespersonUser)表服务接口
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */
public interface SalespersonUserService extends IService<SalespersonUser> {

    SaResult getUserInfoById();

    SaResult updateUserInfoById(SalespersonUser salespersonUser);
}


