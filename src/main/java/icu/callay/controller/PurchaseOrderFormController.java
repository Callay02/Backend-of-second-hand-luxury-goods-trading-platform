package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.PurchaseOrderForm;
import icu.callay.service.PurchaseOrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (PurchaseOrderForm)表控制层
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */
@RestController
@RequestMapping("purchaseOrderForm")
public class PurchaseOrderFormController {

    @Autowired
    private PurchaseOrderFormService purchaseOrderFormService;

    //普通用户创建收购订单
    @PostMapping("createOrderForm")
    public SaResult createOrderForm(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.createOrderForm(purchaseOrderForm);
    }

    //普通用户查找收购订单
    @GetMapping("getPurchaseOrderFormPageByStateAndUid")
    public SaResult getPurchaseOrderFormPageByStateAndUid(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByStateAndUid(state,page,rows);
    }

    //管理员获取收购订单
    @GetMapping("getPurchaseOrderFormPageByState")
    public SaResult getPurchaseOrderFormPageByState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByState(state,page,rows);
    }

    //管理员签收setState=1
    @GetMapping("updatePurchaseOrderFormSateById")
    public SaResult updatePurchaseOrderFormSateById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updatePurchaseOrderFormSateById(id);
    }

    //鉴定师根据Id查找收购订单
    @GetMapping("getPurchaseOrderFormById")
    public SaResult getPurchaseOrderFormById(@RequestParam("id")Long id){
        return purchaseOrderFormService.getPurchaseOrderFormById(id);
    }

    //鉴定师鉴定不通过
    @GetMapping("updateStateSet2ById")
    public SaResult updateStateSet2ById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet2ById(id);
    }

    //鉴定师鉴定通过
    @PostMapping("updateStateSet3ById")
    public SaResult updateStateSet3ById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updateStateSet3ById(purchaseOrderForm);
    }

    //鉴定师查看自己已审核订单
    @GetMapping("getPurchaseOrderFormPageByApid")
    public SaResult getPurchaseOrderFormPageByApid(@RequestParam("apid")String apid,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPurchaseOrderFormPageByApid(apid,page,rows);
    }

    //用户申请退回
    @GetMapping("updateStateSet4ByIdAndUid")
    public SaResult updateStateSet4ByIdAndUid(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet4ByIdAndUid(id);
    }

    //管理员退货
    @PostMapping("updateStateSet5ById")
    public SaResult updateStateSet5ById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updateStateSet5ById(purchaseOrderForm);
    }

    //用户确认出售
    @GetMapping("userConfirmsSale")
    public SaResult userConfirmsSale(@RequestParam("id")Long id){
        return purchaseOrderFormService.userConfirmsSale(id);
    }

    //管理员更新待上架订单数据
    @PostMapping("updatePurchaseOrderFormById")
    public SaResult updatePurchaseOrderFormById(@RequestBody PurchaseOrderForm purchaseOrderForm){
        return purchaseOrderFormService.updatePurchaseOrderFormById(purchaseOrderForm);
    }

    //管理员上架商品
    @GetMapping("productListingById")
    public SaResult productListingById(@RequestParam("id")Long id){
        return purchaseOrderFormService.productListingById(id);
    }

    //用户签收SetState=6
    @GetMapping("updateStateSet6ById")
    public SaResult updateStateSet6ById(@RequestParam("id")Long id){
        return purchaseOrderFormService.updateStateSet6ById(id);
    }

    //用户查找已成功出售商品页面
    @GetMapping("getPageByUidAndState")
    public SaResult getPageByIdAndState(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return purchaseOrderFormService.getPageByIdAndState(state,page,rows);
    }
}

