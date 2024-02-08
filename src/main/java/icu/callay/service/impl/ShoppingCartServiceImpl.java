package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsType;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.mapper.GoodsMapper;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.mapper.ShoppingCartMapper;
import icu.callay.entity.ShoppingCart;
import icu.callay.service.ShoppingCartService;
import icu.callay.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (ShoppingCart)表服务实现类
 *
 * @author Callay
 * @since 2024-02-07 11:40:36
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public SaResult addToShoppingCart(ShoppingCart shoppingCart) {
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("uid",shoppingCart.getUid()).and(wrapper->{
            wrapper.eq("gid", shoppingCart.getGid());
        });
        if(count(shoppingCartQueryWrapper)==0){
            shoppingCart.setCreateTime(new Date());
            QueryWrapper<ShoppingCart> shoppingCartQueryWrapper1 = new QueryWrapper<>();
            shoppingCartQueryWrapper1.eq("uid",shoppingCart.getUid());
            if(count(shoppingCartQueryWrapper1)<10){
                save(shoppingCart);
                return SaResult.ok();
            }
            return SaResult.error("购物测已满，请清理购物车");
        }
        return SaResult.error("购物车中已存在");
    }

    @Override
    public SaResult getShoppingCartById(int id) {
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("uid",id);

        List<GoodsVo> goodsVoList = new ArrayList<>();
        shoppingCartMapper.selectList(shoppingCartQueryWrapper).forEach(shoppingCart -> {
            GoodsVo goodsVo = new GoodsVo();
            Goods goods = goodsMapper.selectById(shoppingCart.getGid());
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
    public SaResult deleteShoppingCartById(int uid, int gid) {
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("uid",uid).and(wrapper->{
            wrapper.eq("gid",gid);
        });
        try {
            remove(shoppingCartQueryWrapper);
            return SaResult.ok("删除成功");
        }catch (Exception e) {
            return SaResult.error("删除失败");
        }
    }
}


