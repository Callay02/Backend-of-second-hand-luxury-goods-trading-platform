package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.RegularUser;

/**
 * (RegularUser)表数据库访问层
 *
 * @author Callay
 * @since 2024-02-07 19:45:58
 */

@Mapper
public interface RegularUserMapper extends BaseMapper<RegularUser> {

}

