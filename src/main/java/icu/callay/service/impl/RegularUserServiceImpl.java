package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.entity.RegularUser;
import icu.callay.mapper.UserMapper;
import icu.callay.service.RegularUserService;
import icu.callay.vo.RegularUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (RegularUser)表服务实现类
 *
 * @author makejava
 * @since 2024-02-07 19:45:58
 */
@Service("regularUserService")
public class RegularUserServiceImpl extends ServiceImpl<RegularUserMapper, RegularUser> implements RegularUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public SaResult getUserInfoById(int id) {
        RegularUserVo regularUserVo = new RegularUserVo();
        BeanUtils.copyProperties(userMapper.selectById(id),regularUserVo);
        try {
            BeanUtils.copyProperties(getById(id),regularUserVo);
            return SaResult.data(regularUserVo);
        }catch (Exception e){
            return SaResult.data(regularUserVo).setMsg("请填写个人信息");
        }


    }

}


