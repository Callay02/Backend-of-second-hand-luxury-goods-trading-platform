package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.User;
import icu.callay.vo.UserRegisterVo;

/**
 * (User)表服务接口
 *
 * @author Callay
 * @since 2024-01-11 23:24:05
 */
public interface UserService extends IService<User> {

    SaResult userLogin(User user);

    SaResult userRegister(UserRegisterVo userRegisterVo);

    SaResult getCode(String email,String type);

    SaResult getUserInfo();

    SaResult getUserPageByType(int type, int page, int rows);

    SaResult deleteUserById(User user);

    SaResult updateMyUserInfoById(User user);

    SaResult adminGetUserNumberByType(String type);

    SaResult adminAddUser(User user);

    SaResult userResetPassword(UserRegisterVo user);

    SaResult adminChangePasswordById(User user);
}


