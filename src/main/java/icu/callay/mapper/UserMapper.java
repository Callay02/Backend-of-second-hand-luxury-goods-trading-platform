package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import icu.callay.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-11 23:24:04
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

}

