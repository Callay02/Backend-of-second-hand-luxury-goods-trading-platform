package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.OrderForm;
import icu.callay.entity.RentalOrderForm;
import icu.callay.service.RentalOrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (RentalOrderForm)表控制层
 *
 * @author Callay
 * @since 2024-03-26 18:09:36
 */
@RestController
@RequestMapping("rentalOrderForm")
public class RentalOrderFormController {

    private RentalOrderFormService rentalOrderFormService;

    @Autowired
    public void RentalOrderFormService(RentalOrderFormService rentalOrderFormService){
        this.rentalOrderFormService = rentalOrderFormService;
    }

    //用户获取订单信息
    @GetMapping("userGetOrderFormByState")
    public SaResult userGetOrderFormByState(@RequestParam("state")int state){
        return rentalOrderFormService.userGetOrderFormByState(state);
    }

    //普通用户根据商品id租赁商品
    @GetMapping("createOrderFormByGid")
    public SaResult createOrderFormByGid(@RequestParam("gid")String gid){
        return rentalOrderFormService.createOrderFormByGid(gid);
    }

    //普通用户根据订单id取消订单
    @GetMapping("cancelOrderById")
    public SaResult cancelOrderById(@RequestParam("id")String id){
        return rentalOrderFormService.cancelOrderById(id);
    }

    //管理员根据订单状态获取订单信息
    @GetMapping("getOrderFormPageByState")
    public SaResult getOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return rentalOrderFormService.getOrderFormPageByState(state,page,rows);
    }

    //管理员根据id取消订单
    @GetMapping("adminCancelOrderById")
    public SaResult adminCancelOrderById(@RequestParam("id")String id){
        return rentalOrderFormService.adminCancelOrderById(id);
    }

    //管理员发货
    @PostMapping("delivery")
    public SaResult delivery(@RequestBody RentalOrderForm rentalOrderForm){
        return rentalOrderFormService.delivery(rentalOrderForm);
    }

    //管理员更新已发货订单信息
    @PostMapping("updateShippedOrderFormById")
    public SaResult updateShippedOrderFormById(@RequestBody RentalOrderForm orderForm){
        return rentalOrderFormService.updateShippedOrderFormById(orderForm);
    }
}

