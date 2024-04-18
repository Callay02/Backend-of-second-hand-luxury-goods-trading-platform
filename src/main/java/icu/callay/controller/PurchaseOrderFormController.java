package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.PurchaseOrderForm;
import icu.callay.service.PurchaseOrderFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (PurchaseOrderForm)表控制层
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("purchaseOrderForm")
public class PurchaseOrderFormController {

    private final PurchaseOrderFormService purchaseOrderFormService;

    /**
     * @param purchaseOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户创建收购订单
     * &#064;2024/4/5 14:30
     */
    @PostMapping("createOrderForm")
    public SaResult createOrderForm(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.createOrderForm(purchaseOrderForm);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户根据订单状态分页查找收购订单
     * &#064;2024/4/5 14:30
     */
    @GetMapping("getPurchaseOrderFormPageByStateAndUid")
    public SaResult getPurchaseOrderFormPageByStateAndUid(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByStateAndUid(state,page,rows);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单状态分页查找收购订单
     * &#064;2024/4/5 14:31
     */
    @GetMapping("getPurchaseOrderFormPageByState")
    public SaResult getPurchaseOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByState(state,page,rows);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员签收(setState=1)
     * &#064;2024/4/5 14:31
     */
    @GetMapping("updatePurchaseOrderFormSateById")
    public SaResult updatePurchaseOrderFormSateById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updatePurchaseOrderFormSateById(id);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 鉴定师根据Id查找收购订单
     * &#064;2024/4/5 14:31
     */
    @GetMapping("getPurchaseOrderFormById")
    public SaResult getPurchaseOrderFormById(@RequestParam("id")Long id){
        return purchaseOrderFormService.getPurchaseOrderFormById(id);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 鉴定师根据订单id更新订单状态(鉴定不通过)
     * &#064;2024/4/5 14:31
     */
    @GetMapping("updateStateSet2ById")
    public SaResult updateStateSet2ById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet2ById(id);
    }

    /**
     * @param purchaseOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 鉴定师根据订单id更新订单状态(鉴定通过)
     * &#064;2024/4/5 14:32
     */
    @PostMapping("updateStateSet3ById")
    public SaResult updateStateSet3ById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updateStateSet3ById(purchaseOrderForm);
    }

    /**
     * @param apid:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 鉴定师分页查询自己处理的审核订单
     * &#064;2024/4/5 14:32
     */
    @GetMapping("getPurchaseOrderFormPageByApid")
    public SaResult getPurchaseOrderFormPageByApid(@RequestParam("apid")String apid,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByApid(apid,page,rows);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户根据订单id申请退回
     * &#064;2024/4/5 14:33
     */
    @GetMapping("updateStateSet4ByIdAndUid")
    public SaResult updateStateSet4ByIdAndUid(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet4ByIdAndUid(id);
    }

    /**
     * @param purchaseOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单id退货
     * &#064;2024/4/5 14:34
     */
    @PostMapping("updateStateSet5ById")
    public SaResult updateStateSet5ById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updateStateSet5ById(purchaseOrderForm);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 普通用户确认出售
     * &#064;2024/4/5 14:34
     */
    @GetMapping("userConfirmsSale")
    public SaResult userConfirmsSale(@RequestParam("id")Long id){
        return purchaseOrderFormService.userConfirmsSale(id);
    }

    /**
     * @param purchaseOrderForm:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单id更新待上架订单数据
     * &#064;2024/4/5 14:34
     */
    @PostMapping("updatePurchaseOrderFormById")
    public SaResult updatePurchaseOrderFormById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updatePurchaseOrderFormById(purchaseOrderForm);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据订单id上架商品
     * &#064;2024/4/5 14:34
     */
    @GetMapping("productListingById")
    public SaResult productListingById(@RequestParam("id")Long id){
        return purchaseOrderFormService.productListingById(id);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 用户签收(SetState=6)
     * &#064;2024/4/5 14:35
     */
    @GetMapping("updateStateSet6ById")
    public SaResult updateStateSet6ById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet6ById(id);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 用户查找已成功出售商品页面
     * &#064;2024/4/5 14:35
     */
    @GetMapping("getPageByUidAndState")
    public SaResult getPageByIdAndState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPageByIdAndState(state,page,rows);
    }

}

