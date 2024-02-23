package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.PurchaseOrderForm;

/**
 * (PurchaseOrderForm)表服务接口
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */
public interface PurchaseOrderFormService extends IService<PurchaseOrderForm> {

    SaResult createOrderForm(PurchaseOrderForm purchaseOrderForm);
}


