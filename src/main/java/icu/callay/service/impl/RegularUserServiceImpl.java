package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.entity.RegularUser;
import icu.callay.mapper.UserMapper;
import icu.callay.service.RegularUserService;
import icu.callay.vo.RegularUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * (RegularUser)表服务实现类
 *
 * @author Callay
 * @since 2024-02-07 19:45:58
 */
@Service("regularUserService")
@RequiredArgsConstructor
public class RegularUserServiceImpl extends ServiceImpl<RegularUserMapper, RegularUser> implements RegularUserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getUserInfoById() {
        String uid = (String) StpUtil.getLoginId();
        RegularUserVo regularUserVo = new RegularUserVo();
        BeanUtils.copyProperties(userMapper.selectById(uid),regularUserVo);
        try {
            BeanUtils.copyProperties(getById(uid),regularUserVo);
            return SaResult.data(regularUserVo);
        }catch (Exception e){
            throw new RuntimeException("请填写个人信息");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateUserInfoById(RegularUser regularUser) {
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
            throw new RuntimeException("更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult recharge(RegularUser regularUser) {
        try {
            Double money = getById(regularUser.getId()).getMoney();
            update(new UpdateWrapper<RegularUser>().eq("id",regularUser.getId()).set("money",money+regularUser.getMoney()).set("update_time",new Date()));
            return SaResult.ok("充值成功");
        }
        catch (Exception e){
            throw new RuntimeException("充值失败");
        }
    }


}


