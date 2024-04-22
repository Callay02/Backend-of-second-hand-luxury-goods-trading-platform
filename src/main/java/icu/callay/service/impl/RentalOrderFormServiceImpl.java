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
import icu.callay.service.RentalOrderFormService;
import icu.callay.vo.PageVo;
import icu.callay.vo.RentalOrderFormVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

/**
 * (RentalOrderForm)表服务实现类
 *
 * @author Callay
 * @since 2024-03-26 18:09:36
 */
@Service("rentalOrderFormService")
@RequiredArgsConstructor
public class RentalOrderFormServiceImpl extends ServiceImpl<RentalOrderFormMapper, RentalOrderForm> implements RentalOrderFormService {

    private final RentalOrderFormMapper rentalOrderFormMapper;
    private final RentalGoodsMapper rentalGoodsMapper;
    private final GoodsBrandMapper goodsBrandMapper;
    private final GoodsTypeMapper goodsTypeMapper;
    private final RegularUserMapper regularUserMapper;
    private final UserMapper userMapper;
    private final GoodsMapper goodsMapper;
    private final OrderFormMapper orderFormMapper;
    private final TrackingController trackingController;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult userGetOrderFormByState(int state,int page,int rows) {
        try {
            String uid = (String) StpUtil.getLoginId();
            QueryWrapper<RentalOrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid", uid).and(wrapper -> wrapper.eq("state", state));
            List<RentalOrderFormVo> orderFormList = new ArrayList<>();
            //以分页的形式放回已结算信息
            if(state==4){
                Page<RentalOrderForm> rentalOrderFormPage = new Page<>(page,rows);
                rentalOrderFormMapper.selectPage(rentalOrderFormPage,orderFormQueryWrapper);
                rentalOrderFormPage.getRecords().forEach(orderForm->{
                    RentalOrderFormVo orderFormVo = new RentalOrderFormVo();

                    RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
                    BeanUtils.copyProperties(goods,orderFormVo);

                    orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                    orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type", goods.getType())).getName());

                    BeanUtils.copyProperties(orderForm,orderFormVo);

                    orderFormList.add(orderFormVo);
                });

                PageVo<RentalOrderFormVo> pageVo = new PageVo<>();
                pageVo.setData(orderFormList);
                pageVo.setTotal(rentalOrderFormPage.getTotal());
                return SaResult.data(pageVo);
            }

            rentalOrderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                RentalOrderFormVo orderFormVo = new RentalOrderFormVo();

                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
                BeanUtils.copyProperties(goods,orderFormVo);

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type", goods.getType())).getName());

                BeanUtils.copyProperties(orderForm,orderFormVo);

                //计算租赁时间和总租金
                if(state==2){
                    Date now = new Date();
                    Date beginTime = orderForm.getBeginTime();
                    Instant nowInstant = now.toInstant();
                    Instant beginTimeInstant = beginTime.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();

                    LocalDateTime localDateTimeNow = nowInstant.atZone(zoneId).toLocalDateTime();
                    LocalDateTime localDateTimeBeginTime = beginTimeInstant.atZone(zoneId).toLocalDateTime();

                    Duration duration =Duration.between(localDateTimeBeginTime,localDateTimeNow);
                    int day = (int) duration.toDays();
                    Double rentTotal = day*goods.getRent();
                    //判断是否超出，超出则租金总价为押金
                    if(rentTotal>=goods.getDeposit())
                        rentTotal=goods.getDeposit();
                    update(new UpdateWrapper<RentalOrderForm>().eq("id",orderForm.getId()).set("day",day).set("rent_total",rentTotal).set("update_time",new Date()));
                    orderFormVo.setDay(day);
                    orderFormVo.setRentTotal(rentTotal);
                }

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        } catch (Exception e) {
            throw new RuntimeException("订单获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult createOrderFormByGid(String gid) {
        try {
            String uid = (String) StpUtil.getLoginId();
            RentalGoods rentalGoods = rentalGoodsMapper.selectById(gid);
            if (rentalGoods.getState() == 1) {
                RegularUser user = regularUserMapper.selectById(uid);
                if (user.getMoney() < rentalGoods.getDeposit()) {
                    return SaResult.error("您的余额不足");
                }
                //先更改商品状态
                rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id", gid).set("state", 0));
                //创建订单并支付押金
                RentalOrderForm rentalOrderForm = new RentalOrderForm();
                rentalOrderForm.setGid(gid);
                rentalOrderForm.setUid(uid);
                rentalOrderForm.setState(0);
                rentalOrderForm.setCreateTime(new Date());
                rentalOrderForm.setUpdateTime(new Date());
                rentalOrderForm.setAddress(user.getAddress());
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id", uid).set("money", user.getMoney() - rentalGoods.getDeposit()));
                rentalOrderFormMapper.insert(rentalOrderForm);
                return SaResult.ok("租赁成功");
            } else {
                return SaResult.error("商品已被其它用户租赁");
            }
        } catch (Exception e) {
            throw new RuntimeException("创建订单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult cancelOrderById(String id) {
        try {
            RentalOrderForm orderForm = getById(id);
            String uid = (String) StpUtil.getLoginId();
            if (count(new QueryWrapper<RentalOrderForm>().eq("id", id).and(wrapper -> wrapper.eq("uid", uid))) == 1 && orderForm.getState()==0) {
                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
                //删除订单
                removeById(id);
                //修改商品状态
                rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id", goods.getId()).set("state", 1));
                //退款
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id", uid).set("money", regularUserMapper.selectById(uid).getMoney() + goods.getDeposit()));
                return SaResult.ok("订单取消成功");
            }
            return SaResult.error("用户不匹配");
        } catch (Exception e) {
            throw new RuntimeException("取消订单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getOrderFormPageByState(int state, int page, int rows) {
        try {
            Page<RentalOrderForm> orderFormPage = new Page<>(page,rows);
            rentalOrderFormMapper.selectPage(orderFormPage,new QueryWrapper<RentalOrderForm>().eq("state",state));

            List<RentalOrderFormVo> orderFormVoList = new ArrayList<>();

            orderFormPage.getRecords().forEach(orderForm -> {
                RentalOrderFormVo orderFormVo = new RentalOrderFormVo();
                BeanUtils.copyProperties(orderForm,orderFormVo);

                //用户信息获取
                RegularUser regularUser = regularUserMapper.selectById(orderForm.getUid());
                BeanUtils.copyProperties(regularUser,orderFormVo);
                User user = userMapper.selectById(orderForm.getUid());
                BeanUtils.copyProperties(user,orderFormVo);

                //商品信息获取
                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
                BeanUtils.copyProperties(goods,orderFormVo);
                GoodsType goodsType = goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType()));
                orderFormVo.setTypeName(goodsType.getName());
                GoodsBrand goodsBrand = goodsBrandMapper.selectById(goods.getBrand());
                orderFormVo.setBrandName(goodsBrand.getName());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setState(orderForm.getState());
                orderFormVo.setAddress(orderForm.getAddress());
                orderFormVo.setUpdateTime(orderForm.getUpdateTime());
                orderFormVo.setDeliveryTime(orderForm.getDeliveryTime());

                //计算租赁时间和总租金
                if(state==2){
                    Date now = new Date();
                    Date beginTime = orderForm.getBeginTime();
                    Instant nowInstant = now.toInstant();
                    Instant beginTimeInstant = beginTime.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();

                    LocalDateTime localDateTimeNow = nowInstant.atZone(zoneId).toLocalDateTime();
                    LocalDateTime localDateTimeBeginTime = beginTimeInstant.atZone(zoneId).toLocalDateTime();

                    Duration duration =Duration.between(localDateTimeBeginTime,localDateTimeNow);
                    int day = (int) duration.toDays();
                    Double rentTotal = day*goods.getRent();
                    //判断是否超出，超出则租金总价为押金
                    if(rentTotal>=goods.getDeposit())
                        rentTotal=goods.getDeposit();
                    update(new UpdateWrapper<RentalOrderForm>().eq("id",orderForm.getId()).set("day",day).set("rent_total",rentTotal).set("update_time",new Date()));
                    orderFormVo.setDay(day);
                    orderFormVo.setRentTotal(rentTotal);
                }
                orderFormVo.setUid(String.valueOf(user.getId()));
                orderFormVoList.add(orderFormVo);
            });

            PageVo<RentalOrderFormVo> orderFormPageVo = new PageVo<>();
            orderFormPageVo.setData(orderFormVoList);
            orderFormPageVo.setTotal(orderFormPage.getTotal());

            return SaResult.data(orderFormPageVo);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("订单获取失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult adminCancelOrderById(String id) {
        try {
            RentalOrderForm orderForm = getById(id);
            String uid = orderForm.getUid();
            RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
            //删除订单
            removeById(id);
            //修改商品状态
            rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id", goods.getId()).set("state", 1));
            //退款
            regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id", uid).set("money", regularUserMapper.selectById(uid).getMoney() + goods.getDeposit()));
            return SaResult.ok("订单取消成功");
        }
        catch (Exception e){
            throw new RuntimeException("订单取消失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult delivery(RentalOrderForm rentalOrderForm) {
        try {
            RentalOrderForm orderForm1 = getById(rentalOrderForm.getId());
            if(orderForm1.getState()==0){

                if(trackingController.create(rentalOrderForm.getCourierCode(),rentalOrderForm.getLogisticsNumber()).getCode()==200){
                    orderForm1.setState(1);
                    orderForm1.setLogisticsNumber(rentalOrderForm.getLogisticsNumber());
                    orderForm1.setCourierCode(rentalOrderForm.getCourierCode());
                    orderForm1.setUpdateTime(new Date());
                    orderForm1.setDeliveryTime(new Date());
                    update(orderForm1,new UpdateWrapper<RentalOrderForm>().eq("id",orderForm1.getId()));
                    return SaResult.ok("发货成功");
                }
                return SaResult.ok("物流信息录入失败");
            }
            return SaResult.error("商品已发货");
        }
        catch (Exception e){
            throw new RuntimeException("发货失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateShippedOrderFormById(RentalOrderForm orderForm) {
        try {
            RentalOrderForm orderForm1 = getById(orderForm.getId());
            if(orderForm1.getState()==1){
                orderForm.setDeliveryTime(new Date());
                orderForm1.setUpdateTime(new Date());
                update(orderForm,new UpdateWrapper<RentalOrderForm>().eq("id",orderForm.getId()));
                return SaResult.ok("更新成功");
            }
            return SaResult.error("订单状态错误");
        }
        catch (Exception e){
            throw new RuntimeException("更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult signById(String id) {
        try {
            String uid = (String) StpUtil.getLoginId();
            if(Objects.equals(getById(id).getUid(), uid)){
                UpdateWrapper<RentalOrderForm> rentalOrderFormUpdateWrapper = new UpdateWrapper<>();
                rentalOrderFormUpdateWrapper.eq("id",id).set("state",2).set("begin_time",new Date()).set("update_time",new Date());
                update(rentalOrderFormUpdateWrapper);
                return SaResult.ok("签收成功");
            }
            return SaResult.error("用户不匹配");
        }
        catch (Exception e){
            throw new RuntimeException("签收失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult userReturn(RentalOrderForm rentalOrderForm) {
        try {
            if(rentalOrderForm.getUid().equals(StpUtil.getLoginId())){
                RentalGoods rentalGoods = rentalGoodsMapper.selectById(rentalOrderForm.getGid());
                Date now = new Date();
                Date beginTime = rentalOrderForm.getBeginTime();
                Instant nowInstant = now.toInstant();
                Instant beginTimeInstant = beginTime.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();

                LocalDateTime localDateTimeNow = nowInstant.atZone(zoneId).toLocalDateTime();
                LocalDateTime localDateTimeBeginTime = beginTimeInstant.atZone(zoneId).toLocalDateTime();

                Duration duration =Duration.between(localDateTimeBeginTime,localDateTimeNow);
                int day = (int) duration.toDays();
                Double rentTotal = day*rentalGoods.getRent();
                //判断是否超出，超出则租金总价为押金
                if(rentTotal>=rentalGoods.getDeposit())
                    rentTotal=rentalGoods.getDeposit();
                UpdateWrapper<RentalOrderForm> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",rentalOrderForm.getId())
                        .set("day",day)
                        .set("rent_total",rentTotal)
                        .set("end_time",new Date())
                        .set("state",3)
                        .set("logistics_number",rentalOrderForm.getLogisticsNumber())
                        .set("courier_code",rentalOrderForm.getCourierCode())
                        .set("update_time",new Date());

                if(trackingController.create(rentalOrderForm.getCourierCode(),rentalOrderForm.getLogisticsNumber()).getCode()==200){
                    update(updateWrapper);
                    return SaResult.ok("退回成功");
                }
                return SaResult.error("物流信息录入失败");
            }
            return SaResult.error("用户不匹配");
        }catch (Exception e){
            throw new RuntimeException("退回失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult userOverdueSettlement(String id) {
        try {
            String uid = (String) StpUtil.getLoginId();
            RentalOrderForm rentalOrderForm = getById(id);
            if(Objects.equals(rentalOrderForm.getUid(), uid)){
                update(new UpdateWrapper<RentalOrderForm>().eq("id",id).set("state",4).set("remark","超时结算").set("update_time",new Date()));
                return SaResult.ok("结算成功");
            }
            return SaResult.error("用户不匹配");
        }
        catch (Exception e){
            throw new RuntimeException("结算失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult adminSignAndSettleById(String id) {
        try {
            RentalOrderForm rentalOrderForm = getById(id);
            //关闭订单
            UpdateWrapper<RentalOrderForm> rentalOrderFormUpdateWrapper = new UpdateWrapper<>();
            rentalOrderFormUpdateWrapper.eq("id",id)
                    .set("state",4)
                    .set("update_time",new Date());
            update(rentalOrderFormUpdateWrapper);

            //重新上架
            rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id",rentalOrderForm.getGid()).set("state",1));

            //结算
            Double money = regularUserMapper.selectById(rentalOrderForm.getUid()).getMoney();
            regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id",rentalOrderForm.getUid()).set("money",money+rentalOrderForm.getRentTotal()));
            return SaResult.ok("结算成功");

        }
        catch (Exception e){
            throw new RuntimeException("结算失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult userWantBuy(String id) {
        try {
            RentalOrderForm rentalOrderForm = getById(id);
            String uid = (String) StpUtil.getLoginId();
            if(Objects.equals(rentalOrderForm.getUid(), uid)){
                RegularUser regularUser =regularUserMapper.selectById(uid);
                RentalGoods rentalGoods = rentalGoodsMapper.selectById(rentalOrderForm.getGid());
                String info = rentalGoods.getInfo()+"_userBuy_"+ new Date().getTime();
                //转为出售商品
                Goods goods = new Goods();
                BeanUtils.copyProperties(rentalGoods,goods);
                goods.setId(null);
                goods.setPrice(rentalGoods.getDeposit()-500.0);
                goods.setAddTime(new Date());
                if(rentalGoods.getUid()==null){
                    rentalGoods.setUid(uid);
                }
                goods.setUserId(Long.valueOf(rentalGoods.getUid()));
                goods.setInfo(info);
                goodsMapper.insert(goods);
                //删除原租赁商品
                rentalGoodsMapper.delete(new QueryWrapper<RentalGoods>().eq("id",rentalOrderForm.getGid()));
                //删除租赁订单
                remove(new QueryWrapper<RentalOrderForm>().eq("id",id));
                //创建出售订单
                OrderForm orderForm = new OrderForm();
                orderForm.setGid(goodsMapper.selectOne(new QueryWrapper<Goods>().eq("info",info)).getId());
                orderForm.setUid(regularUser.getId());
                orderForm.setAddress(regularUser.getAddress());
                orderForm.setState(2);
                orderForm.setCreateTime(new Date());
                orderFormMapper.insert(orderForm);
                //退回保证金
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id",StpUtil.getLoginId()).set("money",regularUser.getMoney()+500.0));
                return SaResult.ok("购买成功");
            }
            return SaResult.error("用户不匹配");
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}


