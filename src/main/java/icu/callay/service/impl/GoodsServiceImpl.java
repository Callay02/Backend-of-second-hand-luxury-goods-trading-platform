package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.GoodsBrand;
import icu.callay.entity.GoodsType;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.mapper.GoodsMapper;
import icu.callay.entity.Goods;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.service.GoodsService;
import icu.callay.vo.GoodsPageVo;
import icu.callay.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (Goods)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:05:04
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsBrandMapper goodsBrandMapper;
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public SaResult getRandomGoodsInfo() {
        List<Goods> randomList = goodsMapper.getRandomGoodsInfo();
        List<GoodsVo> goodsVoList = new ArrayList<>();
        randomList.forEach(goods -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);

            goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
            QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
            goodsTypeQueryWrapper.eq("type",goods.getType());
            goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
            goodsVoList.add(goodsVo);
        });
        return SaResult.data(goodsVoList);
    }

    @Override
    public SaResult getGoodsByType(int type) {
        System.out.println(type);
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.eq("type",type).and(wrapper->wrapper.eq("state",1));

        List<Goods> goodsList = goodsMapper.selectList(goodsQueryWrapper);
        //System.out.println(goodsList);
        List<GoodsVo> goodsVoList = new ArrayList<>();
        goodsList.forEach(goods -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);

            goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
            QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
            goodsTypeQueryWrapper.eq("type",goods.getType());
            goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
            goodsVoList.add(goodsVo);
        });
        return SaResult.data(goodsVoList);
    }

    @Override
    public SaResult getPageByType(int type, int page, int rows) {
        //System.out.println(page+""+rows);
        Page<Goods> goodsPage = new Page<>(page,rows);
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.eq("type",type).and(wrapper->wrapper.eq("state",1));

        goodsMapper.selectPage(goodsPage,goodsQueryWrapper);

        List<GoodsVo> goodsVoList = new ArrayList<>();
        goodsPage.getRecords().forEach(goods -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);

            goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
            QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
            goodsTypeQueryWrapper.eq("type",goods.getType());
            goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
            goodsVoList.add(goodsVo);
        });

        GoodsPageVo goodsPageVo = new GoodsPageVo();
        goodsPageVo.setGoodsVoList(goodsVoList);
        goodsPageVo.setTotal(goodsPage.getTotal());

        return SaResult.data(goodsPageVo);
    }

    @Override
    public SaResult getGoodsById(int id) {
        Goods goods = getById(id);
        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goods,goodsVo);

        goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
        QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
        goodsTypeQueryWrapper.eq("type",goods.getType());
        goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
        return SaResult.data(goodsVo);
    }
}

