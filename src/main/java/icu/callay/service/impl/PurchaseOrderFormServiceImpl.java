package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.PurchaseOrderFormMapper;
import icu.callay.entity.PurchaseOrderForm;
import icu.callay.service.PurchaseOrderFormService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (PurchaseOrderForm)表服务实现类
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */
@Service("purchaseOrderFormService")
public class PurchaseOrderFormServiceImpl extends ServiceImpl<PurchaseOrderFormMapper, PurchaseOrderForm> implements PurchaseOrderFormService {

    @Override
    public SaResult createOrderForm(PurchaseOrderForm purchaseOrderForm) {
        try {
            purchaseOrderForm.setCreateTime(new Date());
            purchaseOrderForm.setUpdateTime(new Date());
            purchaseOrderForm.setState(0);
            save(purchaseOrderForm);
            return SaResult.ok("创建成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}


