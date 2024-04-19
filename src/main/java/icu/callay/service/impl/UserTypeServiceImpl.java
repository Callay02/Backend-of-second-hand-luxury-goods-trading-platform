package icu.callay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.UserTypeMapper;
import icu.callay.entity.UserType;
import icu.callay.service.UserTypeService;
import org.springframework.stereotype.Service;

/**
 * (UserType)表服务实现类
 *
 * @author Callay
 * @since 2024-04-19 09:40:22
 */
@Service("userTypeService")
public class UserTypeServiceImpl extends ServiceImpl<UserTypeMapper, UserType> implements UserTypeService {

}


