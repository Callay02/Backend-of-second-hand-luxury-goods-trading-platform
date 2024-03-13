package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.SalespersonUser;

/**
 * (SalespersonUser)表数据库访问层
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */

@Mapper
public interface SalespersonUserMapper extends BaseMapper<SalespersonUser> {

}

