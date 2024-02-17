package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.GoodsBrand;

/**
 * (GoodsBrand)表服务接口
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */
public interface GoodsBrandService extends IService<GoodsBrand> {

    SaResult getGoodsBrand();

    SaResult getGoodsBrandPage(int page, int rows);

    SaResult addBrand(GoodsBrand goodsBrand);

    SaResult deleteBrandById(GoodsBrand goodsBrand);

    SaResult updateBrand(GoodsBrand goodsBrand);
}


