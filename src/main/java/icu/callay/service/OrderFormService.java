package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.OrderForm;

import java.util.List;

/**
 * (OrderForm)表服务接口
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */
public interface OrderFormService extends IService<OrderForm> {

    SaResult createOrderForm(List<OrderForm> orderFormList);
}


