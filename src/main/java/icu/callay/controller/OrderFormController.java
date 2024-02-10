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

    @GetMapping("getToBeShippedById")
    public SaResult getToBeShippedById(@RequestParam("id")int id){
        return orderFormService.getToBeShippedById(id);
    }

    @GetMapping("cancelOrderById")
    public SaResult cancelOrderById(@RequestParam("id")Long id,@RequestParam("uid")Long uid){
        return orderFormService.cancelOrderById(id,uid);
    }
}

