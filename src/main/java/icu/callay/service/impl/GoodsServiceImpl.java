package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.GoodsMapper;
import icu.callay.entity.Goods;
import icu.callay.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Goods)表服务实现类
 *
 * @author makejava
 * @since 2024-01-30 19:05:04
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public SaResult getRandomGoodsInfo() {
        List<Goods> randomList = goodsMapper.getRandomGoodsInfo();
        return SaResult.data(randomList);
    }
}


