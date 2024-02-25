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
}

