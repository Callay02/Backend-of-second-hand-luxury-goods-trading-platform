package icu.callay.util;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import icu.callay.entity.UserType;
import icu.callay.mapper.UserMapper;
import icu.callay.mapper.UserTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.util
 * &#064;className:      StpInterfaceImpl
 * &#064;author:     Callay
 * &#064;description:  获取账号角色或权限信息
 * &#064;date:    2024/4/19 9:38
 * &#064;version:    1.0
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserTypeMapper userTypeMapper;
    private final UserMapper userMapper;
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        List<String> list = new ArrayList<>();
        userTypeMapper.selectList(new QueryWrapper<UserType>().eq("type",userMapper.selectById((Serializable) o).getType())).forEach(type->{
            list.add(type.getName());
        });
        return list;
    }
}
