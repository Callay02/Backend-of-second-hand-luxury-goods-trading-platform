package icu.callay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.OrderFormStateMapper;
import icu.callay.entity.OrderFormState;
import icu.callay.service.OrderFormStateService;
import org.springframework.stereotype.Service;

/**
 * (OrderFormState)表服务实现类
 *
 * @author Callay
 * @since 2024-02-08 23:02:34
 */
@Service("orderFormStateService")
public class OrderFormStateServiceImpl extends ServiceImpl<OrderFormStateMapper, OrderFormState> implements OrderFormStateService {

}


