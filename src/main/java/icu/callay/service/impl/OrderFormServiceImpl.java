package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.controller.TrackingController;
import icu.callay.entity.*;
import icu.callay.mapper.*;
import icu.callay.service.OrderFormService;
import icu.callay.service.ShoppingCartService;
import icu.callay.vo.OrderFormPageVo;
import icu.callay.vo.OrderFormVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * (OrderForm)表服务实现类
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */
@Service("orderFormService")
@RequiredArgsConstructor
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    private final OrderFormMapper orderFormMapper;
    private final GoodsMapper goodsMapper;
    private final RegularUserMapper regularUserMapper;
    private final GoodsBrandMapper goodsBrandMapper;
    private final GoodsTypeMapper goodsTypeMapper;
    private final ShoppingCartService shoppingCartService;
    private final UserMapper userMapper;
    private final SalespersonUserMapper salespersonUserMapper;
    private final RentalOrderFormMapper rentalOrderFormMapper;
    private final TrackingController trackingController;


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
                if(userMapper.selectById(uid).getType()==1){
                    salespersonUserMapper.update(new UpdateWrapper<SalespersonUser>().eq("id",uid).set("money",salespersonUserMapper.selectById(uid).getMoney()+goods.getPrice()));
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
                orderFormVo.setCourierCode(orderForm.getCourierCode());

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
            String uid = (String) StpUtil.getLoginId();
            QueryWrapper<OrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid",uid).and(wrapper->{
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
                orderFormVo.setCourierCode(orderForm.getCourierCode());

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
                update(new UpdateWrapper<OrderForm>().eq("id", orderForm.getId()).set("state", 2).set("salesperson_settle",0));
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
                //录入物流信息
                if(trackingController.create(orderForm.getCourierCode(),orderForm.getLogisticsNumber()).getCode()==200){
                    orderForm.setState(1);
                    orderForm.setDeliveryTime(new Date());
                    update(orderForm,new UpdateWrapper<OrderForm>().eq("id",orderForm.getId()));
                    return SaResult.ok("发货成功");
                }
                return SaResult.error("物流信息录入失败");
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

    @Override
    public SaResult adminGetSalespersonOrderFormByisSettleAndSid(String sid,int state,String isSettle,int page,int rows) {
        try {
            Page<OrderForm> orderFormPage = new Page<>(page,rows);
            QueryWrapper<OrderForm> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid",sid).eq("state",state);
            if(isSettle!=null){
                queryWrapper.eq("salesperson_settle",isSettle);
            }
            orderFormMapper.selectPage(orderFormPage,queryWrapper);

            List<OrderFormVo> orderFormVoList = new ArrayList<>();
            orderFormPage.getRecords().forEach(orderForm -> {
                OrderFormVo orderFormVo = new OrderFormVo();
                BeanUtils.copyProperties(orderForm,orderFormVo);

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
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult settleSalespersonOrderFormByid(String id) {
        try {
            OrderForm orderForm = getById(id);
            //将要更新的金额
            double money = salespersonUserMapper.selectById(orderForm.getUid()).getMoney()+goodsMapper.selectById(orderForm.getGid()).getPrice()*0.02;
            //结算
            salespersonUserMapper.update(new UpdateWrapper<SalespersonUser>().eq("id",orderForm.getUid()).set("money",money));
            //更新订单结算状态
            update(new UpdateWrapper<OrderForm>().eq("id",id).set("salesperson_settle",1));
            return SaResult.ok("订单"+id+"结算成功");
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public SaResult getSalesVolume(String beginTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Long lbt = Long.valueOf(beginTime);
            Long let = Long.valueOf(endTime);

            //购买订单销量获取
            QueryWrapper<OrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("state",2).ge("create_time",sdf.format(lbt)).lt("create_time",sdf.format(let));
            AtomicReference<Double> orderFormTotalAmount= new AtomicReference<>(0.0);

            List<OrderForm> orderFormList =orderFormMapper.selectList(orderFormQueryWrapper);
            int orderFormCount = orderFormList.size();
            orderFormList.forEach(orderForm -> {
                orderFormTotalAmount.updateAndGet(v -> v + goodsMapper.selectById(orderForm.getGid()).getPrice());
            });

            //租赁订单销量获取
            QueryWrapper<RentalOrderForm> rentalOrderFormQueryWrapper = new QueryWrapper<>();
            rentalOrderFormQueryWrapper.eq("state",4).ge("update_time",sdf.format(lbt)).lt("update_time",sdf.format(let));

            AtomicReference<Double> rentalOrderFormTotalAmount= new AtomicReference<>(0.0);
            List<RentalOrderForm> rentalOrderFormList = rentalOrderFormMapper.selectList(rentalOrderFormQueryWrapper);
            int rentalOrderFormCount = rentalOrderFormList.size();
            rentalOrderFormList.forEach(rentalOrderForm -> {
                rentalOrderFormTotalAmount.updateAndGet(v -> v + rentalOrderForm.getRentTotal());
            });
            List<Map<String,String>> mapList = new ArrayList<>();
            Map<String,String> map1 = new HashMap<>();
            map1.put("value", String.valueOf(orderFormTotalAmount));
            map1.put("name","出售商品"+"("+orderFormCount+"件)");

            Map<String,String> map2 = new HashMap<>();
            map2.put("value",String.valueOf(rentalOrderFormTotalAmount));
            map2.put("name","租赁商品"+"("+rentalOrderFormCount+"件)");

            mapList.add(map1);
            mapList.add(map2);
            return SaResult.data(mapList);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

}


