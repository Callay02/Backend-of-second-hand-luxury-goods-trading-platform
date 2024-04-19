package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.UserType;

/**
 * (UserType)表数据库访问层
 *
 * @author Callay
 * @since 2024-04-19 09:40:22
 */

@Mapper
public interface UserTypeMapper extends BaseMapper<UserType> {

}

