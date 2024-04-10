package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.PlatformRevenueFlowForm;

/**
 * (PlatformRevenueFlowForm)表服务接口
 *
 * @author Callay
 * @since 2024-04-10 14:33:56
 */
public interface PlatformRevenueFlowFormService extends IService<PlatformRevenueFlowForm> {

    SaResult getPageByForm(String type,String from, int page, int rows);
}


