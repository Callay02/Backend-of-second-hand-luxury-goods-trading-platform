package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.OrderForm;
import icu.callay.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (OrderForm)表控制层
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */
@RestController
@RequestMapping("orderForm")
public class OrderFormController {

    private OrderFormService orderFormService;
    @Autowired
    public void OrderFormService(OrderFormService orderFormService){
        this.orderFormService=orderFormService;
    }

    /**
     * @param orderFormList:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户创建订单（购买）
     * &#064;2024/4/5 14:16
     */
    @PostMapping("createOrderForm")
    public SaResult createOrderForm(@RequestBody List<OrderForm> orderFormList){
        return orderFormService.createOrderForm(orderFormList);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id获取待发货订单信息
     * &#064;2024/4/5 14:16
     */
    @GetMapping("getToBeShippedById")
    public SaResult getToBeShippedById(@RequestParam("id")int id){
        return orderFormService.getToBeShippedById(id);
    }

    /**
     * @param id:
     * @param uid:
     * @return SaResult
     * @author Callay
     * &#064;description 根据订单Id取消订单
     * &#064;2024/4/5 14:16
     */
    @GetMapping("cancelOrderById")
    public SaResult cancelOrderById(@RequestParam("id")Long id,@RequestParam("uid")Long uid){
        return orderFormService.cancelOrderById(id,uid);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户Id获取已发货订单信息
     * &#064;2024/4/5 14:25
     */
    @GetMapping("getShippedById")
    public SaResult getShippedById(@RequestParam("id")Long id){
        return orderFormService.getShippedById(id);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户Id获取已签收订单信息
     * &#064;2024/4/5 14:25
     */
    @GetMapping("getSignedById")
    public SaResult getSignedById(@RequestParam("id")Long id){
        return orderFormService.getSignedById(id);
    }

    /**
     * @param orderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户签收
     * &#064;2024/4/5 14:26
     */
    @PostMapping("Sign")
    public SaResult Sign(@RequestBody OrderForm orderForm){
        return orderFormService.Sign(orderForm);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 根据订单状态分页查询订单信息
     * &#064;2024/4/5 14:26
     */
    @GetMapping("getOrderFormPageByState")
    public SaResult getOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return orderFormService.getOrderFormPageByState(state,page,rows);
    }

    /**
     * @param orderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 发货
     * &#064;2024/4/5 14:26
     */
    @PostMapping("delivery")
    public SaResult delivery(@RequestBody OrderForm orderForm){
        return orderFormService.delivery(orderForm);
    }

    /**
     * @param orderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单id更新已发货订单信息
     * &#064;2024/4/5 14:26
     */
    @PostMapping("updateShippedOrderFormById")
    public SaResult updateShippedOrderFormById(@RequestBody OrderForm orderForm){
        return orderFormService.updateShippedOrderFormById(orderForm);
    }

    /**
     * @param orderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 销售员购买商品(创建订单)
     * &#064;2024/4/5 14:27
     */
    @PostMapping("createOrderFormBySid")
    public SaResult createOrderFormBySid(@RequestBody OrderForm orderForm){
        return orderFormService.createOrderFormBySid(orderForm);
    }

}

