package icu.callay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import icu.callay.entity.ShoppingCart;

/**
 * (ShoppingCart)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-07 11:40:36
 */

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}

