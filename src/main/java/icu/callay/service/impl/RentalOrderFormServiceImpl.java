package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.*;
import icu.callay.mapper.*;
import icu.callay.service.RentalOrderFormService;
import icu.callay.vo.RentalOrderFormVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    public void RentalOrderFormMapper(RentalOrderFormMapper rentalOrderFormMapper){
        this.rentalOrderFormMapper=rentalOrderFormMapper;
    }

    @Autowired
    public void RentalGoodsMapper(RentalGoodsMapper rentalGoodsMapper){
        this.rentalGoodsMapper=rentalGoodsMapper;
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
    public void RegularUserMapper(RegularUserMapper regularUserMapper){
        this.regularUserMapper=regularUserMapper;
    }

    @Override
    public SaResult userGetOrderFormByState(int state) {
        try{
            String uid = (String) StpUtil.getLoginId();
            QueryWrapper<RentalOrderForm> orderFormQueryWrapper = new QueryWrapper<>();
            orderFormQueryWrapper.eq("uid",uid).and(wrapper->{
                wrapper.eq("state",state);
            });
            List<RentalOrderFormVo> orderFormList = new ArrayList<>();
            rentalOrderFormMapper.selectList(orderFormQueryWrapper).forEach(orderForm -> {
                RentalOrderFormVo orderFormVo = new RentalOrderFormVo();
                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());


                orderFormVo.setGid(orderForm.getGid());
                orderFormVo.setImg(goods.getImg());
                orderFormVo.setInfo(goods.getInfo());
                orderFormVo.setFineness(goods.getFineness());

                orderFormVo.setBrandName(goodsBrandMapper.selectById(goods.getBrand()).getName());
                orderFormVo.setTypeName(goodsTypeMapper.selectOne(new QueryWrapper<GoodsType>().eq("type",goods.getType())).getName());

                orderFormVo.setDeposit(goods.getDeposit());
                orderFormVo.setRent(goods.getRent());
                orderFormVo.setCreateTime(orderForm.getCreateTime());

                orderFormVo.setId(orderForm.getId());
                orderFormVo.setAddress(orderForm.getAddress());

                orderFormList.add(orderFormVo);

            });
            return SaResult.data(orderFormList);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult createOrderFormByGid(String gid) {
        try {
            String uid = (String) StpUtil.getLoginId();
            RentalGoods rentalGoods = rentalGoodsMapper.selectById(gid);
            if(rentalGoods.getState()==1){
                RegularUser user=regularUserMapper.selectById(uid);
                if(user.getMoney()<rentalGoods.getDeposit()){
                    return SaResult.error("您的余额不足");
                }
                //先更改商品状态
                rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id",gid).set("state",0));
                //创建订单并支付押金
                RentalOrderForm rentalOrderForm = new RentalOrderForm();
                rentalOrderForm.setGid(gid);
                rentalOrderForm.setUid(uid);
                rentalOrderForm.setState(0);
                rentalOrderForm.setCreateTime(new Date());
                rentalOrderForm.setUpdateTime(new Date());
                rentalOrderForm.setAddress(user.getAddress());
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id",uid).set("money",user.getMoney()-rentalGoods.getDeposit()));
                rentalOrderFormMapper.insert(rentalOrderForm);
                return SaResult.ok("租赁成功");
            }else{
                return SaResult.error("商品已被其它用户租赁");
            }
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult cancelOrderById(String id) {
        try {
            RentalOrderForm orderForm = getById(id);
            String uid = (String) StpUtil.getLoginId();
            if(count(new QueryWrapper<RentalOrderForm>().eq("id",id).and(wrapper->{wrapper.eq("uid",uid);}))==1){
                RentalGoods goods = rentalGoodsMapper.selectById(orderForm.getGid());
                //删除订单
                removeById(id);
                //修改商品状态
                rentalGoodsMapper.update(new UpdateWrapper<RentalGoods>().eq("id",goods.getId()).set("state",1));
                //退款
                regularUserMapper.update(new UpdateWrapper<RegularUser>().eq("id",uid).set("money",regularUserMapper.selectById(uid).getMoney()+goods.getDeposit()));
                return SaResult.ok("订单取消成功");
            }
            return SaResult.error("用户不匹配");
        }catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


