package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.*;
import icu.callay.mapper.*;
import icu.callay.service.RentalGoodsService;
import icu.callay.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * (RentalGoods)表服务实现类
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */
@Service("rentalGoodsService")
@RequiredArgsConstructor
public class RentalGoodsServiceImpl extends ServiceImpl<RentalGoodsMapper, RentalGoods> implements RentalGoodsService {


    private final RentalGoodsMapper rentalGoodsMapper;
    private final GoodsBrandMapper goodsBrandMapper;
    private final GoodsTypeMapper goodsTypeMapper;
    private final GoodsMapper goodsMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsPageByState(String state,int page, int rows) {
        try {
            Page<RentalGoods> rentalGoodsPage = new Page<>(page,rows);
            if(Objects.equals(state, "")){
                QueryWrapper<RentalGoods> rentalGoodsQueryWrapper = new QueryWrapper<>();
                rentalGoodsQueryWrapper.eq("state",0).or().eq("state",1);
                rentalGoodsMapper.selectPage(rentalGoodsPage,rentalGoodsQueryWrapper);
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
        catch (Exception e){
            throw new RuntimeException("获取租赁商品信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult addGoods(RentalGoods rentalGoods) {
        try {
            rentalGoods.setState(1);
            rentalGoods.setAddTime(new Date());
            save(rentalGoods);
            return SaResult.ok("添加成功");
        }
        catch (Exception e){
            throw new RuntimeException("添加租赁商品失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsByIdNoVo(String id) {
        try {
            return SaResult.data(getById(id));
        }
        catch (Exception e){
            throw new RuntimeException("获取商品信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateGoodsById(RentalGoods rentalGoods) {
        try {
            update(rentalGoods,new UpdateWrapper<RentalGoods>().eq("id",rentalGoods.getId()));
            return SaResult.ok("更新成功");
        }
        catch (Exception e){
            throw new RuntimeException("更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult deleteGoodsById(RentalGoods rentalGoods) {
        try {
            remove(new QueryWrapper<RentalGoods>().eq("id",rentalGoods.getId()));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            throw new RuntimeException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            throw new RuntimeException("查找商品信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsById(String id) {
        try {
            RentalGoods goods = getById(id);
            RentalGoodsVo goodsVo = new RentalGoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);

            goodsVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
            QueryWrapper<GoodsType> goodsTypeQueryWrapper = new QueryWrapper<>();
            goodsTypeQueryWrapper.eq("type",goods.getType());
            goodsVo.setTypeName(goodsTypeMapper.selectOne(goodsTypeQueryWrapper).getName());
            return SaResult.data(goodsVo);
        }catch (Exception e){
            throw new RuntimeException("获取商品信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult adminGetGoodsPageByBrandAndTypeAndInfo(SearchGoodsVo searchGoodsVo) {
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
            if(!searchGoodsVo.getId().isEmpty()){
                goodsQueryWrapper.eq("id",searchGoodsVo.getId());
            }
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
            throw new RuntimeException("查找商品信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult rentalGoodsToGoods(String rgid) {
        try {
            RentalGoods rentalGoods = getById(rgid);
            Goods goods = new Goods();
            BeanUtils.copyProperties(rentalGoods,goods);
            goods.setId(null);
            goods.setPrice(rentalGoods.getDeposit()-500.0);
            goods.setAddTime(new Date());
            if(rentalGoods.getUid()==null){
                rentalGoods.setUid((String) StpUtil.getLoginId());
            }
            goods.setUserId(Long.valueOf(rentalGoods.getUid()));
            goodsMapper.insert(goods);
            update(new UpdateWrapper<RentalGoods>().eq("id",rgid).set("state",3));
            return SaResult.ok("转移成功");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}


