package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.RentalOrderForm;

/**
 * (RentalOrderForm)表服务接口
 *
 * @author Callay
 * @since 2024-03-26 18:09:36
 */
public interface RentalOrderFormService extends IService<RentalOrderForm> {

    SaResult userGetOrderFormByState(int state);

    SaResult createOrderFormByGid(String gid);

    SaResult cancelOrderById(String id);

    SaResult getOrderFormPageByState(int state, int page, int rows);

    SaResult adminCancelOrderById(String id);

    SaResult delivery(RentalOrderForm rentalOrderForm);

    SaResult updateShippedOrderFormById(RentalOrderForm orderForm);
}


