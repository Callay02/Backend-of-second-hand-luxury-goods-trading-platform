package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.GoodsType;

/**
 * (GoodsType)表服务接口
 *
 * @author Callay
 * @since 2024-01-30 19:07:47
 */
public interface GoodsTypeService extends IService<GoodsType> {

    SaResult getGoodsType();

    SaResult getGoodsTypePage(int page, int rows);

    SaResult addGoodsType(GoodsType goodsType);

    SaResult deleteTypeById(int type);

    SaResult updateTypeName(GoodsType goodsType);
}


