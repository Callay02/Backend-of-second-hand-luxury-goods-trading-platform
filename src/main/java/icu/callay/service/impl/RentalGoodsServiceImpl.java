package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsType;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.mapper.RentalGoodsMapper;
import icu.callay.entity.RentalGoods;
import icu.callay.service.RentalGoodsService;
import icu.callay.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (RentalGoods)表服务实现类
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */
@Service("rentalGoodsService")
public class RentalGoodsServiceImpl extends ServiceImpl<RentalGoodsMapper, RentalGoods> implements RentalGoodsService {

    @Autowired
    private RentalGoodsMapper rentalGoodsMapper;

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public SaResult getGoodsPageByState(String state,int page, int rows) {
        Page<RentalGoods> rentalGoodsPage = new Page<>(page,rows);
        if(state==""){
            rentalGoodsMapper.selectPage(rentalGoodsPage,new QueryWrapper<RentalGoods>());
        }
        else{
            rentalGoodsMapper.selectPage(rentalGoodsPage,new QueryWrapper<RentalGoods>().eq("state",state));
        }

        List<RentalGoodsVo> goodsVoList = new ArrayList<>();
        rentalGoodsPage.getRecords().forEach(goods -> {
            RentalGoodsVo goodsVo = new RentalGoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);

            goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
            QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
            goodsTypeQueryWrapper.eq("type",goods.getType());
            goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
            goodsVoList.add(goodsVo);
        });
        PageVo<RentalGoodsVo> rentalGoodsVoPageVo = new PageVo<>();
        rentalGoodsVoPageVo.setData(goodsVoList);
        rentalGoodsVoPageVo.setTotal(rentalGoodsPage.getTotal());

        return SaResult.data(rentalGoodsVoPageVo);
    }

    @Override
    public SaResult addGoods(RentalGoods rentalGoods) {
        try {
            rentalGoods.setState(1);
            rentalGoods.setAddTime(new Date());
            save(rentalGoods);
            return SaResult.ok("添加成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getGoodsByIdNoVo(String id) {
        try {
            return SaResult.data(getById(id));
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateGoodsById(RentalGoods rentalGoods) {
        try {
            update(rentalGoods,new UpdateWrapper<RentalGoods>().eq("id",rentalGoods.getId()));
            return SaResult.ok("更新成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult deleteGoodsById(RentalGoods rentalGoods) {
        try {
            remove(new QueryWrapper<RentalGoods>().eq("id",rentalGoods.getId()));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getGoodsPageByBrandAndTypeAndInfo(SearchGoodsVo searchGoodsVo) {
        try{
            QueryWrapper<RentalGoods> goodsQueryWrapper = new QueryWrapper<>();
            if(!searchGoodsVo.getBrand().equals("未选择")){
                goodsQueryWrapper.eq("brand",searchGoodsVo.getBrand());
            }
            if(!searchGoodsVo.getType().equals("未选择")){
                goodsQueryWrapper.eq("type",searchGoodsVo.getType());
            }
            if (!searchGoodsVo.getInfo().isEmpty()){
                goodsQueryWrapper.like("info",searchGoodsVo.getInfo());
            }
            goodsQueryWrapper.eq("state",1);
            Page<RentalGoods> goodsPage = new Page<>(searchGoodsVo.getPage(),searchGoodsVo.getRows());
            rentalGoodsMapper.selectPage(goodsPage,goodsQueryWrapper);

            List<RentalGoodsVo> goodsVoList = new ArrayList<>();
            goodsPage.getRecords().forEach(goods -> {
                RentalGoodsVo goodsVo = new RentalGoodsVo();
                BeanUtils.copyProperties(goods,goodsVo);

                goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
                goodsTypeQueryWrapper.eq("type",goods.getType());
                goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
                goodsVoList.add(goodsVo);
            });
            PageVo<RentalGoodsVo> goodsPageVo =new PageVo<>();
            goodsPageVo.setData(goodsVoList);
            goodsPageVo.setTotal(goodsPage.getTotal());

            return SaResult.data(goodsPageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


