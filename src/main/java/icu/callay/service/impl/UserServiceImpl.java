package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.RegularUser;
import icu.callay.entity.SalespersonUser;
import icu.callay.entity.User;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.mapper.SalespersonUserMapper;
import icu.callay.mapper.UserMapper;
import icu.callay.service.UserService;
import icu.callay.vo.RegularUserVo;
import icu.callay.vo.SalespersonUserVo;
import icu.callay.vo.UserPageVo;
import icu.callay.vo.UserRegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author Callay
 * @since 2024-01-11 23:24:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;
    private RegularUserMapper regularUserMapper;
    private SalespersonUserMapper salespersonUserMapper;
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public void UserMapper(UserMapper userMapper){
        this.userMapper=userMapper;
    }
    @Autowired
    public void RegularUserMapper(RegularUserMapper regularUserMapper){
        this.regularUserMapper=regularUserMapper;
    }
    @Autowired
    public void SalespersonUserMapper(SalespersonUserMapper salespersonUserMapper){
        this.salespersonUserMapper=salespersonUserMapper;
    }
    @Autowired
    public void StringRedisTemplate(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }


    @Override
    public SaResult userLogin(User user) {
        try {
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
                if(selectUser.getIsDeleted()==1)
                    return SaResult.error("该账号已注销");
                StpUtil.login(selectUser.getId());
                return SaResult.data(StpUtil.getTokenInfo());
            }
            else
                return SaResult.error("账号或密码错误");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }

    }
    @Override
    public SaResult userRegister(UserRegisterVo userRegisterVo) {
        try {
            if(Objects.equals(userRegisterVo.getVerificationCode(), stringRedisTemplate.opsForValue().get(userRegisterVo.getEmail() + "_register"))){
                if( IdcardUtil.isValidCard(userRegisterVo.getIdCard())){
                    QueryWrapper<User> wrapperName = new QueryWrapper<User>().eq("name",userRegisterVo.getName());
                    if(userMapper.selectCount(wrapperName)<1 ){
                        try {
                            //AES加密密码
                            String a = userRegisterVo.getIdCard();
                            String aesKey = Base64.encode(a);
                            AES aes = SecureUtil.aes(aesKey.getBytes());
                            String aesPasswd = aes.encryptHex(userRegisterVo.getPassword());
                            userRegisterVo.setPassword(aesPasswd);

                            userRegisterVo.setCreateTime(new Date());
                            userRegisterVo.setIsDeleted(0);

                            User user = new User();
                            BeanUtils.copyProperties(userRegisterVo,user);
                            userMapper.insert(user);
                            //普通用户注册
                            if(user.getType()==0){
                                RegularUser regularUser = new RegularUser();
                                regularUser.setId(user.getId());
                                regularUser.setUpdateTime(user.getCreateTime());
                                regularUser.setAddress("");
                                regularUser.setMoney((double) 0);
                                regularUser.setPhone("");
                                regularUserMapper.insert(regularUser);
                            }
                            //销售员注册
                            else if (user.getType()==1) {
                                SalespersonUser salespersonUser = new SalespersonUser();
                                salespersonUser.setId(String.valueOf(user.getId()));
                                salespersonUser.setPhone("");
                                salespersonUser.setUpdateTime(new Date());
                                salespersonUser.setMoney((double) 0);
                                salespersonUserMapper.insert(salespersonUser);
                            }

                            stringRedisTemplate.opsForValue().getAndDelete(user.getEmail()+"_verificationCode");
                            return SaResult.ok("注册成功");
                        }
                        catch (Exception e){
                            return SaResult.error(e.getMessage());
                        }
                    }else
                        return SaResult.error("用户已存在");
                }else
                    return SaResult.error("身份证错误");
            }else
                return SaResult.error("验证码错误");
        }catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
    @Override
    public SaResult getCode(String email,String type) {
        String verificationCode = RandomUtil.randomString(4);
        try {
            stringRedisTemplate.opsForValue().set(email+"_"+type,verificationCode,5, TimeUnit.MINUTES);
            MailUtil.send(email, "二手奢侈品交易平台", "<h1>您的验证码为："+verificationCode+"</h1>", false);
            return SaResult.ok("验证码已发送至"+email);
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

        if(password.equals(pwd)){
            return SaResult.data(user);
        }
        return SaResult.error();

    }
    @Override
    public SaResult getUserPageByType(int type, int page, int rows) {
        try {
            Page<User> userPage = new Page<>();
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("type",type);
            userQueryWrapper.eq("is_deleted",0);
            userMapper.selectPage(userPage,userQueryWrapper);

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
            //销售员
            else if(type==1){
                List<SalespersonUserVo> salespersonUserVoList = new ArrayList<>();
                userPage.getRecords().forEach(user -> {
                    SalespersonUserVo salespersonUserVo = new SalespersonUserVo();

                    BeanUtils.copyProperties(user,salespersonUserVo);
                    SalespersonUser salespersonUser = salespersonUserMapper.selectById(user.getId());
                    BeanUtils.copyProperties(salespersonUser,salespersonUserVo);

                    salespersonUserVoList.add(salespersonUserVo);
                });
                UserPageVo<SalespersonUserVo> salespersonUserVoUserPageVo = new UserPageVo<>();
                salespersonUserVoUserPageVo.setUserVoList(salespersonUserVoList);
                salespersonUserVoUserPageVo.setTotal(userPage.getTotal());
                return SaResult.data(salespersonUserVoUserPageVo);
            }
            //管理员
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
            update(new UpdateWrapper<User>().eq("id",user.getId()).set("is_deleted",1));
            return SaResult.ok("删除成功");
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
    @Override
    public SaResult adminGetUserNumberByType(String type) {
        try {
            Date now = new Date();
            Instant nowInstant = now.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTimeNow = nowInstant.atZone(zoneId).toLocalDateTime();
            int year = localDateTimeNow.getYear();
            int month = localDateTimeNow.getMonth().getValue();

            Date lastMonth = new Date(year-1900,month-1,1);

            return SaResult.data(count(new QueryWrapper<User>().eq("type",type).ge("create_time",lastMonth)));
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
    @Override
    public SaResult adminAddUser(User user) {
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
                    user.setIsDeleted(0);

                    userMapper.insert(user);
                    //普通用户注册
                    if(user.getType()==0){
                        RegularUser regularUser = new RegularUser();
                        regularUser.setId(user.getId());
                        regularUser.setUpdateTime(user.getCreateTime());
                        regularUser.setAddress("");
                        regularUser.setMoney((double) 0);
                        regularUser.setPhone("");
                        regularUserMapper.insert(regularUser);
                    }
                    //销售员注册
                    else if (user.getType()==1) {
                        SalespersonUser salespersonUser = new SalespersonUser();
                        salespersonUser.setId(String.valueOf(user.getId()));
                        salespersonUser.setPhone("");
                        salespersonUser.setUpdateTime(new Date());
                        salespersonUser.setMoney((double) 0);
                        salespersonUserMapper.insert(salespersonUser);
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

    @Override
    public SaResult userResetPassword(UserRegisterVo user){
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",user.getName()).eq("email",user.getEmail()).eq("is_deleted",0);

            //判断用户是否存在
            if(userMapper.selectCount(queryWrapper)==1){
                //验证验证码
                if(Objects.equals(stringRedisTemplate.opsForValue().get(user.getEmail() + "_resetPassword"), user.getVerificationCode())){
                    String password = RandomUtil.randomString(12);

                    //AES加密密码
                    String a = userMapper.selectOne(queryWrapper).getIdCard();
                    String aesKey = Base64.encode(a);
                    AES aes = SecureUtil.aes(aesKey.getBytes());
                    String aesPasswd = aes.encryptHex(password);

                    update(new UpdateWrapper<User>().eq("name",user.getName()).set("password",aesPasswd));
                    MailUtil.send(user.getEmail(), "二手奢侈品交易平台", "<h1>您的密码已重置："+password+"</h1>", false);
                    return SaResult.ok("您的密码已重置，新密码已发送至邮箱，请注意查收");
                }else{
                    return SaResult.error("验证码错误");
                }
            }else{
                SaResult.error("用户不存在");
            }
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
        return null;
    }


}


