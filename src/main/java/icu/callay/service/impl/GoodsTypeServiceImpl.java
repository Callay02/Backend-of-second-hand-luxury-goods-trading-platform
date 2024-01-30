package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.entity.GoodsType;
import icu.callay.service.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (GoodsType)表服务实现类
 *
 * @author makejava
 * @since 2024-01-30 19:07:47
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public SaResult getGoodsType() {
        List<GoodsType> goodsTypeList =goodsTypeMapper.getGoodsType();
        return SaResult.data(goodsTypeList);
    }
}


