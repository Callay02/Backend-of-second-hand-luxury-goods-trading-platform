package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.User;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.entity.RegularUser;
import icu.callay.mapper.UserMapper;
import icu.callay.service.RegularUserService;
import icu.callay.vo.RegularUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (RegularUser)表服务实现类
 *
 * @author Callay
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

    @Override
    public SaResult updateUserInfoById(RegularUser regularUser) {
        //System.out.println(regularUser);
        regularUser.setUpdateTime(new Date());
        regularUser.setMoney(null);
        try {
            if(count(new QueryWrapper<RegularUser>().eq("id",regularUser.getId()))>0){
                update(regularUser,new QueryWrapper<RegularUser>().eq("id",regularUser.getId()));
                return SaResult.ok("修改成功");
            }else{
                save(regularUser);
                return SaResult.ok("录入成功");
            }
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult recharge(RegularUser regularUser) {
        try {
            Double money = getById(regularUser.getId()).getMoney();
            update(new UpdateWrapper<RegularUser>().eq("id",regularUser.getId()).set("money",money+regularUser.getMoney()));
            return SaResult.ok("充值成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }


}


