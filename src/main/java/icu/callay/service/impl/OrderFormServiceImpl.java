package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.Goods;
import icu.callay.entity.RegularUser;
import icu.callay.mapper.GoodsMapper;
import icu.callay.mapper.OrderFormMapper;
import icu.callay.entity.OrderForm;
import icu.callay.mapper.RegularUserMapper;
import icu.callay.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (OrderForm)表服务实现类
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */
@Service("orderFormService")
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RegularUserMapper regularUserMapper;


    @Override
    public SaResult createOrderForm(List<OrderForm> orderFormList) {
        //查询用户余额
        Double userMoney = regularUserMapper.selectById(orderFormList.get(0).getUid()).getMoney();
        Double totalPrice = orderFormList.stream().mapToDouble(orderForm ->goodsMapper.selectById(orderForm.getGid()).getPrice()).sum();
        //用户余额大于商品总值
        if(userMoney>=totalPrice){
            orderFormList.forEach(orderForm -> {
                orderForm.setCreateTime(new Date());
                Long uid = orderForm.getUid();
                int gid = orderForm.getGid();
                    //判断是否已被其它用户购买
                    if(count(new QueryWrapper<OrderForm>()
                            .eq("gid",gid)
                    )==0){
                        goodsMapper.update(null,new UpdateWrapper<Goods>().eq("id",gid).set("state",0));
                        orderForm.setState(0);
                        save(orderForm);
                        regularUserMapper.update(null,new UpdateWrapper<RegularUser>().eq("id",uid).set("money",regularUserMapper.selectById(uid).getMoney()-goodsMapper.selectById(gid).getPrice()));
                    }
            });
            return SaResult.ok();
        }
        return SaResult.error("余额不足");
    }
}


