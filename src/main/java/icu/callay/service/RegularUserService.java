package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.RegularUser;

/**
 * (RegularUser)表服务接口
 *
 * @author makejava
 * @since 2024-02-07 19:45:58
 */
public interface RegularUserService extends IService<RegularUser> {


    SaResult getUserInfoById(int id);
}


