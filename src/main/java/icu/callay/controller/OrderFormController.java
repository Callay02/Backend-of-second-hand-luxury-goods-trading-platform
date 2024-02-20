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

    @Autowired
    private OrderFormService orderFormService;

    @PostMapping("createOrderForm")
    public SaResult createOrderForm(@RequestBody List<OrderForm> orderFormList){
        return orderFormService.createOrderForm(orderFormList);
    }

    //用户Id
    @GetMapping("getToBeShippedById")
    public SaResult getToBeShippedById(@RequestParam("id")int id){
        return orderFormService.getToBeShippedById(id);
    }

    //订单Id
    @GetMapping("cancelOrderById")
    public SaResult cancelOrderById(@RequestParam("id")Long id,@RequestParam("uid")Long uid){
        return orderFormService.cancelOrderById(id,uid);
    }

    //用户Id
    @GetMapping("getShippedById")
    public SaResult getShippedById(@RequestParam("id")Long id){
        return orderFormService.getShippedById(id);
    }

    //用户Id
    @GetMapping("getSignedById")
    public SaResult getSignedById(@RequestParam("id")Long id){
        return orderFormService.getSignedById(id);
    }

    //签收
    @PostMapping("Sign")
    public SaResult Sign(@RequestBody OrderForm orderForm){
        return orderFormService.Sign(orderForm);
    }

    @GetMapping("getOrderFormPageByState")
    public SaResult getOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return orderFormService.getOrderFormPageByState(state,page,rows);
    }

    //发货
    @PostMapping("delivery")
    public SaResult delivery(@RequestBody OrderForm orderForm){
        return orderFormService.delivery(orderForm);
    }

    @PostMapping("updateShippedOrderFormById")
    public SaResult updateShippedOrderFormById(@RequestBody OrderForm orderForm){
        return orderFormService.updateShippedOrderFormById(orderForm);
    }
}

