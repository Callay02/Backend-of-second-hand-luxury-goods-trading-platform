package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.User;
import icu.callay.mapper.SalespersonUserMapper;
import icu.callay.entity.SalespersonUser;
import icu.callay.mapper.UserMapper;
import icu.callay.service.SalespersonUserService;
import icu.callay.vo.SalespersonUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * (SalespersonUser)表服务实现类
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */
@Service("salespersonUserService")
public class SalespersonUserServiceImpl extends ServiceImpl<SalespersonUserMapper, SalespersonUser> implements SalespersonUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public SaResult getUserInfoById() {
        SalespersonUserVo salespersonUserVo = new SalespersonUserVo();
        try {
            //用户表中获取数据
            User user = userMapper.selectById((Serializable) StpUtil.getLoginId());

            //解密
            String a = user.getIdCard();
            String aesKey = Base64.encode(a);
            AES aes = SecureUtil.aes(aesKey.getBytes());
            String encryptHex = user.getPassword();
            String password = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
            user.setPassword(password);

            BeanUtils.copyProperties(user,salespersonUserVo);

            //销售员表中获取数据
            SalespersonUser salespersonUser = getById((Serializable) StpUtil.getLoginId());
            BeanUtils.copyProperties(salespersonUser,salespersonUserVo);

            return SaResult.data(salespersonUserVo);
        }
        catch (IllegalArgumentException illegalArgumentException){
            return SaResult.ok().setMsg("请填写个人信息").setData(salespersonUserVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }

    }

    @Override
    public SaResult updateUserInfoById(SalespersonUser salespersonUser) {
        try {
            String id = (String) StpUtil.getLoginId();
            salespersonUser.setId(id);
            salespersonUser.setUpdateTime(new Date());
            if (count(new QueryWrapper<SalespersonUser>().eq("id",id))==0){
                save(salespersonUser);
            }else{
                updateById(salespersonUser);
            }
            return SaResult.ok("更新成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

}


