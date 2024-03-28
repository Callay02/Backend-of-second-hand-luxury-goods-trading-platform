package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.RentalGoods;
import icu.callay.vo.SearchGoodsVo;

/**
 * (RentalGoods)表服务接口
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */
public interface RentalGoodsService extends IService<RentalGoods> {

    SaResult getGoodsPageByState(String state,int page, int rows);

    SaResult addGoods(RentalGoods rentalGoods);

    SaResult getGoodsByIdNoVo(String id);

    SaResult updateGoodsById(RentalGoods rentalGoods);

    SaResult deleteGoodsById(RentalGoods rentalGoods);

    SaResult getGoodsPageByBrandAndTypeAndInfo(SearchGoodsVo searchGoodsVo);

    SaResult getGoodsById(String id);

}


