package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import icu.callay.vo.SearchGoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * (Goods)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:05:04
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private GoodsMapper goodsMapper;
    private GoodsBrandMapper goodsBrandMapper;
    private GoodsTypeMapper goodsTypeMapper;
    @Autowired
    public void GoodsMapper(GoodsMapper goodsMapper){
        this.goodsMapper=goodsMapper;
    }
    @Autowired
    public void GoodsBrandMapper(GoodsBrandMapper goodsBrandMapper){
        this.goodsBrandMapper=goodsBrandMapper;
    }
    @Autowired
    public void GoodsTypeMapper(GoodsTypeMapper goodsTypeMapper){
        this.goodsTypeMapper=goodsTypeMapper;
    }

    @Override
    public SaResult getRandomGoodsInfo() {
        List<Goods> randomList = goodsMapper.getRandomGoodsInfo();
        List<GoodsVo> goodsVoList = new ArrayList<>();
        randomList.forEach(goods -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);
            //System.out.println(goods.getBrand());
            String brandName = goodsBrandMapper.selectById(goods.getBrand()).getName();
            //System.out.println(brandName);

            goodsVo.setBrandName(brandName);
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

    @Override
    public SaResult getGoodsPageByState(String state,int page, int rows) {
        Page<Goods> goodsPage = new Page<>(page,rows);
        QueryWrapper<Goods> queryWrapper =new QueryWrapper<>();
        if(!Objects.equals(state, "")){
            queryWrapper.eq("state",state);
        }
        goodsMapper.selectPage(goodsPage,queryWrapper);
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
    public SaResult addGoods(Goods goods) {
        try {
            goods.setState(1);
            goods.setAddTime(new Date());
            save(goods);
            return SaResult.ok("添加成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult deleteGoodsById(Goods goods) {
        try {
            remove(new QueryWrapper<Goods>().eq("id",goods.getId()));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateGoods(Goods goods) {
        try {
            update(goods,new UpdateWrapper<Goods>().eq("id",goods.getId()));
            return SaResult.ok("更新成功");
        }
        catch (Exception e) {
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getGoodsByIdNoVo(Long id) {
        try {
            return SaResult.data(getById(id));
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateGoodsById(Goods goods) {
        try {
            update(goods,new UpdateWrapper<Goods>().eq("id",goods.getId()));
            return SaResult.ok("更新成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getGoodsPageByBrandAndTypeAndInfo(SearchGoodsVo searchGoodsVo) {
        try{
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            //System.out.println(searchGoodsVo);
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
            Page<Goods> goodsPage = new Page<>(searchGoodsVo.getPage(),searchGoodsVo.getRows());
            goodsMapper.selectPage(goodsPage,goodsQueryWrapper);

            List<GoodsVo> goodsVoList = new ArrayList<>();
            goodsPage.getRecords().forEach(goods -> {
                //System.out.println(goods);
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
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult adminGetGoodsPageByBrandAndTypeAndInfo(SearchGoodsVo searchGoodsVo) {
        try{
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
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
            Page<Goods> goodsPage = new Page<>(searchGoodsVo.getPage(),searchGoodsVo.getRows());
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
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

}


