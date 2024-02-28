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
}

