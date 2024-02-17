package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.Goods;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Goods)表数据库访问层
 *
 * @author Callay
 * @since 2024-01-30 19:05:04
 */

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from goods where state = 1 order by rand() limit 24")
    List<Goods> getRandomGoodsInfo();

}

