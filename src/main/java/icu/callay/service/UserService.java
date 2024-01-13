package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.User;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2024-01-11 23:24:05
 */
public interface UserService extends IService<User> {

    SaResult userLogin(User user);

    SaResult userRegister(User user);

    SaResult getCode(String email);
}


