package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.GoodsType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (GoodsType)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-30 19:07:47
 */

@Mapper
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    @Select("select * from goods_type")
    List<GoodsType> getGoodsType();
}

