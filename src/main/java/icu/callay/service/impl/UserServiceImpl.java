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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.User;
import icu.callay.mapper.UserMapper;
import icu.callay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2024-01-11 23:24:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

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
            return SaResult.data(StpUtil.getTokenInfo()).setMsg(String.valueOf(selectUser.getType()));
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
                    StpUtil.login(user.getId());
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




}


