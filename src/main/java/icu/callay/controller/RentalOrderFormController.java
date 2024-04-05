package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
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


    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 用户根据订单状态分页查询订单信息
     * &#064;2024/4/5 14:47
     */
    @GetMapping("userGetOrderFormByState")
    public SaResult userGetOrderFormByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return rentalOrderFormService.userGetOrderFormByState(state,page,rows);
    }

    /**
     * @param gid:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户根据商品id租赁商品(创建订单)
     * &#064;2024/4/5 14:48
     */
    @GetMapping("createOrderFormByGid")
    public SaResult createOrderFormByGid(@RequestParam("gid")String gid){
        return rentalOrderFormService.createOrderFormByGid(gid);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户根据订单id取消订单
     * &#064;2024/4/5 14:48
     */
    @GetMapping("cancelOrderById")
    public SaResult cancelOrderById(@RequestParam("id")String id){
        return rentalOrderFormService.cancelOrderById(id);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单状态分页查询订单信息
     * &#064;2024/4/5 14:49
     */
    @GetMapping("getOrderFormPageByState")
    public SaResult getOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return rentalOrderFormService.getOrderFormPageByState(state,page,rows);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据id取消订单
     * &#064;2024/4/5 14:49
     */
    @GetMapping("adminCancelOrderById")
    public SaResult adminCancelOrderById(@RequestParam("id")String id){
        return rentalOrderFormService.adminCancelOrderById(id);
    }

    /**
     * @param rentalOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员发货
     * &#064;2024/4/5 14:49
     */
    @PostMapping("delivery")
    public SaResult delivery(@RequestBody RentalOrderForm rentalOrderForm){
        return rentalOrderFormService.delivery(rentalOrderForm);
    }

    /**
     * @param orderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据商品id更新已发货订单信息
     * &#064;2024/4/5 14:50
     */
    @PostMapping("updateShippedOrderFormById")
    public SaResult updateShippedOrderFormById(@RequestBody RentalOrderForm orderForm){
        return rentalOrderFormService.updateShippedOrderFormById(orderForm);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户签收
     * &#064;2024/4/5 14:50
     */
    @GetMapping("signById")
    public SaResult signById(@RequestParam("id")String id){
        return rentalOrderFormService.signById(id);
    }

    /**
     * @param rentalOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 用户退回
     * &#064;2024/4/5 14:50
     */
    @PostMapping("userReturn")
    public SaResult userReturn(@RequestBody RentalOrderForm rentalOrderForm){
        return rentalOrderFormService.userReturn(rentalOrderForm);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 用户超时结算
     * &#064;2024/4/5 14:50
     */
    @GetMapping("userOverdueSettlementById")
    public SaResult userOverdueSettlement(@RequestParam("id")String id){
        return rentalOrderFormService.userOverdueSettlement(id);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员签收并结算
     * &#064;2024/4/5 14:50
     */
    @GetMapping("adminSignAndSettleById")
    public SaResult adminSignAndSettleById(@RequestParam("id")String id){
        return rentalOrderFormService.adminSignAndSettleById(id);
    }
}

