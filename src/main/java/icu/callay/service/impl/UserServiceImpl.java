package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.RegularUser;
import icu.callay.entity.User;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.mapper.UserMapper;
import icu.callay.service.UserService;
import icu.callay.vo.RegularUserVo;
import icu.callay.vo.UserPageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * (User)表服务实现类
 *
 * @author Callay
 * @since 2024-01-11 23:24:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RegularUserMapper regularUserMapper;

    @Override
    public SaResult userLogin(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",user.getName()).eq("type",user.getType());
        List<User> list = userMapper.selectList(wrapper);
        User selectUser = list.get(0);

        //AES解密密码
        String a = selectUser.getIdCard();
        String aesKey = Base64.encode(a);
        AES aes = SecureUtil.aes(aesKey.getBytes());
        String encryptHex = selectUser.getPassword();
        String password = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        if(password.equals(user.getPassword())) {
            StpUtil.login(selectUser.getId());
            return SaResult.data(StpUtil.getTokenInfo());
        }
        else
            return SaResult.error();
    }

    @Override
    public SaResult userRegister(User user) {
        if( IdcardUtil.isValidCard(user.getIdCard())){
            QueryWrapper<User> wrapperName = new QueryWrapper<User>().eq("name",user.getName());
            if(userMapper.selectCount(wrapperName)<1 ){
                try {
                    //AES加密密码
                    String a = user.getIdCard();
                    String aesKey = Base64.encode(a);
                    AES aes = SecureUtil.aes(aesKey.getBytes());
                    String aesPasswd = aes.encryptHex(user.getPassword());
                    user.setPassword(aesPasswd);

                    user.setCreateTime(new Date());

                    userMapper.insert(user);
                    if(user.getType()==0){
                        RegularUser regularUser = new RegularUser();
                        regularUser.setId(user.getId());
                        regularUser.setUpdateTime(user.getCreateTime());
                        regularUser.setAddress("");
                        regularUser.setMoney((double) 0);
                        regularUser.setPhone("");
                        regularUserMapper.insert(regularUser);
                    }
                    return SaResult.data(user);
                }
                catch (Exception e){
                    return SaResult.error(e.getMessage());
                }
            }else
                return SaResult.error("用户已存在");

        }else
            return SaResult.error("身份证错误");

    }

    //生成验证码并发送
    @Override
    public SaResult getCode(String email) {
        String verificationCode = RandomUtil.randomString(4);
        //System.out.println(verificationCode);
        try {
            //System.out.println(email);
            MailUtil.send(email, "二手奢侈品交易平台注册验证码", "<h1>您的验证码："+verificationCode+"</h1>", false);
            return SaResult.data(verificationCode);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getUserInfo(Long id,String pwd) {
        User user = getById(id);

        //AES解密密码
        String a = user.getIdCard();
        String aesKey = Base64.encode(a);
        AES aes = SecureUtil.aes(aesKey.getBytes());
        String encryptHex = user.getPassword();
        String password = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        //System.out.println(password);
        //System.out.println(pwd);
        if(password.equals(pwd)){
            return SaResult.data(user);
        }
        return SaResult.error();

    }


    @Override
    public SaResult getUserPageByType(int type, int page, int rows) {
        try {
            Page<User> userPage = new Page<>();
            userMapper.selectPage(userPage,new QueryWrapper<User>().eq("type",type));

            //普通用户
            if(type==0){
                List<RegularUserVo> regularUserVoList = new ArrayList<>();
                userPage.getRecords().forEach(user -> {
                    RegularUserVo regularUserVo = new RegularUserVo();

                    BeanUtils.copyProperties(user,regularUserVo);
                    RegularUser regularUser = regularUserMapper.selectById(user.getId());
                    BeanUtils.copyProperties(regularUser,regularUserVo);

                    regularUserVoList.add(regularUserVo);
                });
                UserPageVo<RegularUserVo> regularUserVoUserPageVo = new UserPageVo<>();
                regularUserVoUserPageVo.setUserVoList(regularUserVoList);
                regularUserVoUserPageVo.setTotal(userPage.getTotal());
                return SaResult.data(regularUserVoUserPageVo);
            }
            //鉴定师
            else if (type==2) {
                UserPageVo<User> userUserPageVo = new UserPageVo<>();
                userUserPageVo.setUserVoList(userPage.getRecords());
                userUserPageVo.setTotal(userPage.getTotal());
                return SaResult.data(userUserPageVo);
            }
            //TODO
            else if(type==1){
                return SaResult.ok("销售员");

            }
            //TODO
            else if(type==2){
                return SaResult.ok("鉴定师");
            }
            else if(type==3){
                UserPageVo<User> userUserPageVo = new UserPageVo<>();
                userUserPageVo.setUserVoList(userPage.getRecords());
                userUserPageVo.setTotal(userUserPageVo.getTotal());
                return SaResult.data(userUserPageVo);
            }
            return SaResult.error("查找失败");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }

    }

    @Override
    public SaResult deleteUserById(User user) {
        try {
            //普通用户
            if(user.getType()==0){
                removeById(user.getId());
                regularUserMapper.deleteById(user.getId());
                return SaResult.ok("删除成功");
            }
            return SaResult.error("删除失败");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateMyUserInfoById(User user) {
        try {
            String id = (String) StpUtil.getLoginId();
            user.setId(Long.valueOf(id));
            User getUser = getById(id);

            //AES加密密码
            String a = getUser.getIdCard();
            String aesKey = Base64.encode(a);
            AES aes = SecureUtil.aes(aesKey.getBytes());
            String aesPasswd = aes.encryptHex(user.getPassword());
            user.setPassword(aesPasswd);

            user.setUpdateTime(new Date());
            updateById(user);
            return SaResult.ok("修改成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }


}


