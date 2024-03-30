package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.*;
import icu.callay.mapper.*;
import icu.callay.service.RentalOrderFormService;
import icu.callay.vo.PageVo;
import icu.callay.vo.RentalOrderFormVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

/**
 * (RentalOrderForm)表服务实现类
 *
 * @author Callay
 * @since 2024-03-26 18:09:36
 */
@Service("rentalOrderFormService")
public class RentalOrderFormServiceImpl extends ServiceImpl<RentalOrderFormMapper, RentalOrderForm> implements RentalOrderFormService {

    private RentalOrderFormMapper rentalOrderFormMapper;
    private RentalGoodsMapper rentalGoodsMapper;
    private GoodsBrandMapper goodsBrandMapper;
    private GoodsTypeMapper goodsTypeMapper;
    private RegularUserMapper regularUserMapper;
    private UserMapper userMapper;

    @Autowired
    public void RentalOrderFormMapper(RentalOrderFormMapper rentalOrderFormMapper) {
        this.rentalOrderFormMapper = rentalOrderFormMapper;
    }

    @Autowired
    public void RentalGoodsMapper(RentalGoodsMapper rentalGoodsMapper) {
        this.rentalGoodsMapper = rentalGoodsMapper;
    }

    @Autowired
    public void GoodsBrandMapper(GoodsBrandMapper goodsBrandMapper) {
        this.goodsBrandMapper = goodsBrandMapper;
    }

    @Autowired
    public void GoodsTypeMapper(GoodsTypeMapper goodsTypeMapper) {
        this.goodsTypeMapper = goodsTypeMapper;
    }

    @Autowired
    public void RegularUserMapper(RegularUserMapper regularUserMapper) {
        this.regularUserMapper = regularUserMapper;
    }

    @Autowired
    public void UserMapper(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    @Override
    public SaResult userGetOrderFormByState(int state) {
        try {
            String uid = (String) StpUtil.getLoginId();
            QueryWrapper<RentalOrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid", uid).and(wrapper -> wrapper.eq("state", state));
            List<RentalOrderFormVo> orderFormList = new ArrayList<>();
            rentalOrderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                RentalOrderFormVo orderFormVo = new RentalOrderFormVo();
                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());


                orderFormVo.setGid(orderForm.getGid());
                orderFormVo.setImg(goods.getImg());
                orderFormVo.setInfo(goods.getInfo());
                orderFormVo.setFineness(goods.getFineness());

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type", goods.getType())).getName());

                orderFormVo.setDeposit(goods.getDeposit());
                orderFormVo.setRent(goods.getRent());
                orderFormVo.setCreateTime(orderForm.getCreateTime());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setAddress(orderForm.getAddress());
                orderFormVo.setDeliveryTime(orderForm.getDeliveryTime());
                orderFormVo.setLogisticsNumber(orderForm.getLogisticsNumber());
                orderFormVo.setDeliveryTime(orderForm.getDeliveryTime());
                orderFormVo.setUid(orderForm.getUid());
                orderFormVo.setUpdateTime(orderForm.getUpdateTime());

                orderFormVo.setBeginTime(orderForm.getBeginTime());
                orderFormVo.setEndTime(orderForm.getEndTime());

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
                orderFormVo.setDay(orderForm.getDay());
                orderFormVo.setRentTotal(orderForm.getRentTotal());

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        } catch (Exception e) {
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult cancelOrderById(String id) {
        try {
            RentalOrderForm orderForm = getById(id);
            String uid = (String) StpUtil.getLoginId();
            if (count(new QueryWrapper<RentalOrderForm>().eq("id", id).and(wrapper -> wrapper.eq("uid", uid))) == 1) {
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
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
                orderFormVo.setDeliveryTime(new Date());
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

                orderFormVoList.add(orderFormVo);
            });

            PageVo<RentalOrderFormVo> orderFormPageVo = new PageVo<>();
            orderFormPageVo.setData(orderFormVoList);
            orderFormPageVo.setTotal(orderFormPage.getTotal());

            return SaResult.data(orderFormPageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult delivery(RentalOrderForm rentalOrderForm) {
        try {
            RentalOrderForm orderForm1 = getById(rentalOrderForm.getId());
            if(orderForm1.getState()==0){
                rentalOrderForm.setState(1);
                rentalOrderForm.setUpdateTime(new Date());
                rentalOrderForm.setDeliveryTime(new Date());
                update(rentalOrderForm,new UpdateWrapper<RentalOrderForm>().eq("id",rentalOrderForm.getId()));
                return SaResult.ok("发货成功");
            }
            return SaResult.error("商品已发货");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateShippedOrderFormById(RentalOrderForm orderForm) {
        try {
            RentalOrderForm orderForm1 = getById(orderForm.getId());
            if(orderForm1.getState()==1){
                orderForm.setDeliveryTime(new Date());
                orderForm1.setUpdateTime(new Date());
                update(orderForm,new UpdateWrapper<RentalOrderForm>().eq("id",orderForm.getId()));
                return SaResult.ok("更新成功");
            }
            return SaResult.ok("订单状态错误");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
                        .set("update_time",new Date());
                update(updateWrapper);
                return SaResult.ok("退回成功");
            }
            return SaResult.error("用户不匹配");
        }catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
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
            return SaResult.error(e.getMessage());
        }
    }

}


