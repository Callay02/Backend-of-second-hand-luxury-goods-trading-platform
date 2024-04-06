package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.*;
import icu.callay.mapper.*;
import icu.callay.service.OrderFormService;
import icu.callay.service.ShoppingCartService;
import icu.callay.vo.OrderFormPageVo;
import icu.callay.vo.OrderFormVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * (OrderForm)表服务实现类
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */
@Service("orderFormService")
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    private OrderFormMapper orderFormMapper;
    private GoodsMapper goodsMapper;
    private RegularUserMapper regularUserMapper;
    private GoodsBrandMapper goodsBrandMapper;
    private GoodsTypeMapper goodsTypeMapper;
    private ShoppingCartService shoppingCartService;
    private UserMapper userMapper;
    private SalespersonUserMapper salespersonUserMapper;
    @Autowired
    public void OrderFormMapper(OrderFormMapper orderFormMapper){
        this.orderFormMapper=orderFormMapper;
    }
    @Autowired
    public void GoodsMapper(GoodsMapper goodsMapper){
        this.goodsMapper=goodsMapper;
    }
    @Autowired
    public void RegularUserMapper(RegularUserMapper regularUserMapper){
        this.regularUserMapper=regularUserMapper;
    }
    @Autowired
    public void GoodsBrandMapper(GoodsBrandMapper goodsBrandMapper){
        this.goodsBrandMapper=goodsBrandMapper;
    }
    @Autowired
    public void GoodsTypeMapper(GoodsTypeMapper goodsTypeMapper){
        this.goodsTypeMapper=goodsTypeMapper;
    }
    @Autowired
    public void ShoppingCartService(ShoppingCartService shoppingCartService){
        this.shoppingCartService=shoppingCartService;
    }
    @Autowired
    public void UserMapper(UserMapper userMapper){
        this.userMapper=userMapper;
    }
    @Autowired
    public void SalespersonUserMapper(SalespersonUserMapper salespersonUserMapper){
        this.salespersonUserMapper=salespersonUserMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult createOrderForm(List<OrderForm> orderFormList) {
        try {
            //查询用户余额
            Double userMoney = regularUserMapper.selectById(orderFormList.get(0).getUid()).getMoney();
            Double totalPrice = orderFormList.stream().mapToDouble(orderForm ->goodsMapper.selectById(orderForm.getGid()).getPrice()).sum();
            //用户余额大于商品总值
            if(userMoney>=totalPrice){
                orderFormList.forEach(orderForm -> {
                    orderForm.setCreateTime(new Date());
                    Long uid = orderForm.getUid();
                    Long gid = orderForm.getGid();
                    //判断是否已被其它用户购买
                    if(count(new QueryWrapper<OrderForm>()
                            .eq("gid",gid)
                    )==0){
                        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",gid).set("state",0));
                        orderForm.setState(0);
                        //System.out.println(orderForm);
                        orderForm.setAddress(regularUserMapper.selectById(uid).getAddress());
                        save(orderForm);
                        regularUserMapper.update(null,new UpdateWrapper<RegularUser>().eq("id",uid).set("money",regularUserMapper.selectById(uid).getMoney()-goodsMapper.selectById(gid).getPrice()));
                        //删除购物车物品
                        shoppingCartService.deleteShoppingCartById(uid,gid);
                    }
                });
                return SaResult.ok();
            }
            return SaResult.error("余额不足");
        }
        catch (Exception e){
            throw new RuntimeException("创建失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getToBeShippedById(int id) {
        try{
            QueryWrapper<OrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid",id).and(wrapper->{
                wrapper.eq("state",0);
            });
            List<OrderFormVo> orderFormList = new ArrayList<>();
            orderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                OrderFormVo orderFormVo = new OrderFormVo();
                Goods goods = goodsMapper.selectById(orderForm.getGid());


                orderFormVo.setGid(orderForm.getGid());
                orderFormVo.setImg(goods.getImg());
                orderFormVo.setInfo(goods.getInfo());
                orderFormVo.setFineness(goods.getFineness());

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType())).getName());

                orderFormVo.setPrice(goods.getPrice());
                orderFormVo.setCreateTime(orderForm.getCreateTime());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setAddress(orderForm.getAddress());

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        }
        catch (Exception e){
            throw new RuntimeException("获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult cancelOrderById(Long id,Long uid) {
        try {
            OrderForm orderForm = getOne(new QueryWrapper<OrderForm>().eq("id",id));
            if(count(new QueryWrapper<OrderForm>().eq("id",id).and(wrapper->{wrapper.eq("uid",uid);}))==1 && orderForm.getState()==0){
                Goods goods = goodsMapper.selectById(orderForm.getGid());
                //删除订单
                removeById(id);
                //修改商品状态
                goodsMapper.update(new UpdateWrapper<Goods>().eq("id",goods.getId()).set("state",1));

                //销售员取消订单
                if(regularUserMapper.selectCount(new UpdateWrapper<RegularUser>().eq("id",uid))==0){
                    return SaResult.ok("订单取消成功");
                }
                //退款
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id",uid).set("money",regularUserMapper.selectById(uid).getMoney()+goods.getPrice()));
                return SaResult.ok("订单取消成功");
            }
            return SaResult.error("用户不匹配");
        }catch (Exception e){
            throw new RuntimeException("取消失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getShippedById(Long id) {
        try{
            QueryWrapper<OrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid",id).and(wrapper->{
                wrapper.eq("state",1);
            });
            List<OrderFormVo> orderFormList = new ArrayList<>();
            orderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                OrderFormVo orderFormVo = new OrderFormVo();
                Goods goods = goodsMapper.selectById(orderForm.getGid());


                orderFormVo.setGid(orderForm.getGid());
                orderFormVo.setImg(goods.getImg());
                orderFormVo.setInfo(goods.getInfo());
                orderFormVo.setFineness(goods.getFineness());

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType())).getName());

                orderFormVo.setPrice(goods.getPrice());
                orderFormVo.setCreateTime(orderForm.getCreateTime());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setLogisticsNumber(orderForm.getLogisticsNumber());
                orderFormVo.setAddress(orderForm.getAddress());

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        }
        catch (Exception e){
            throw new RuntimeException("获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getSignedById(Long id) {
        try{
            QueryWrapper<OrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid",id).and(wrapper->{
                wrapper.eq("state",2);
            });
            List<OrderFormVo> orderFormList = new ArrayList<>();
            orderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                OrderFormVo orderFormVo = new OrderFormVo();
                Goods goods = goodsMapper.selectById(orderForm.getGid());


                orderFormVo.setGid(orderForm.getGid());
                orderFormVo.setImg(goods.getImg());
                orderFormVo.setInfo(goods.getInfo());
                orderFormVo.setFineness(goods.getFineness());

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType())).getName());

                orderFormVo.setPrice(goods.getPrice());
                orderFormVo.setCreateTime(orderForm.getCreateTime());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setLogisticsNumber(orderForm.getLogisticsNumber());
                orderFormVo.setAddress(orderForm.getAddress());

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        }
        catch (Exception e){
            throw new RuntimeException("获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult Sign(OrderForm orderForm) {
        try {
            if(Objects.equals(getById(orderForm.getId()).getUid(), orderForm.getUid())) {
                update(new UpdateWrapper<OrderForm>().eq("id", orderForm.getId()).set("state", 2));
                return SaResult.ok();
            }
            return SaResult.error("ID不匹配");
        }
        catch (Exception e){
            throw new RuntimeException("签收失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getOrderFormPageByState(int state, int page, int rows) {
        try {
            Page<OrderForm> orderFormPage = new Page<>(page,rows);
            orderFormMapper.selectPage(orderFormPage,new QueryWrapper<OrderForm>().eq("state",state));

            List<OrderFormVo> orderFormVoList = new ArrayList<>();
            orderFormPage.getRecords().forEach(orderForm -> {
                OrderFormVo orderFormVo = new OrderFormVo();
                BeanUtils.copyProperties(orderForm,orderFormVo);

                //普通用户信息获取
                if(regularUserMapper.selectCount(new QueryWrapper<RegularUser>().eq("id",orderForm.getUid()))!=0){
                    RegularUser regularUser = regularUserMapper.selectById(orderForm.getUid());
                    BeanUtils.copyProperties(regularUser,orderFormVo);
                }
                //销售员用户信息获取
                else if (salespersonUserMapper.selectCount(new QueryWrapper<SalespersonUser>().eq("id",orderForm.getUid()))!=0) {
                    SalespersonUser salespersonUser = salespersonUserMapper.selectById(orderForm.getUid());
                    orderFormVo.setPhone(salespersonUser.getPhone());
                }

                User user = userMapper.selectById(orderForm.getUid());
                BeanUtils.copyProperties(user,orderFormVo);

                //商品信息获取
                Goods goods = goodsMapper.selectById(orderForm.getGid());
                BeanUtils.copyProperties(goods,orderFormVo);
                GoodsType goodsType = goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType()));
                orderFormVo.setTypeName(goodsType.getName());
                GoodsBrand goodsBrand = goodsBrandMapper.selectById(goods.getBrand());
                orderFormVo.setBrandName(goodsBrand.getName());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setState(orderForm.getState());
                orderFormVo.setAddress(orderForm.getAddress());

                orderFormVoList.add(orderFormVo);
            });

            OrderFormPageVo orderFormPageVo = new OrderFormPageVo();
            orderFormPageVo.setOrderFormVoList(orderFormVoList);
            orderFormPageVo.setTotal(orderFormPage.getTotal());

            return SaResult.data(orderFormPageVo);
        }
        catch (Exception e){
            throw new RuntimeException("订单获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult delivery(OrderForm orderForm) {
        try {
            OrderForm orderForm1 = getById(orderForm.getId());
            if(orderForm1.getState()==0){
                orderForm.setState(1);
                orderForm.setDeliveryTime(new Date());
                update(orderForm,new UpdateWrapper<OrderForm>().eq("id",orderForm.getId()));
                return SaResult.ok("发货成功");
            }
            return SaResult.error("商品已发货");
        }
        catch (Exception e){
            throw new RuntimeException("商品发货失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateShippedOrderFormById(OrderForm orderForm) {
        try {
            OrderForm orderForm1 = getById(orderForm.getId());
            if(orderForm1.getState()==1){
                orderForm.setDeliveryTime(new Date());
                update(orderForm,new UpdateWrapper<OrderForm>().eq("id",orderForm.getId()));
                return SaResult.ok("更新成功");
            }
            return SaResult.ok("订单状态错误");
        }
        catch (Exception e){
            throw new RuntimeException("订单更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult createOrderFormBySid(OrderForm orderForm) {
        try {
            orderForm.setState(0);
            orderForm.setCreateTime(new Date());
            //下架商品
            goodsMapper.update(new UpdateWrapper<Goods>().eq("id",orderForm.getGid()).set("state",0));
            //创建订单
            save(orderForm);

            return SaResult.ok("购买成功");
        }
        catch (Exception e){
            throw new RuntimeException("订单创建失败");
        }

    }

}


