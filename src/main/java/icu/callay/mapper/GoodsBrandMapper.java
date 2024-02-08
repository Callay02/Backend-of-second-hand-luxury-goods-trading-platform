package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.GoodsBrand;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (GoodsBrand)表数据库访问层
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */

@Mapper
public interface GoodsBrandMapper extends BaseMapper<GoodsBrand> {

    @Select("select * from goods_brand")
    List<GoodsBrand> getGoodsBrand();
}

