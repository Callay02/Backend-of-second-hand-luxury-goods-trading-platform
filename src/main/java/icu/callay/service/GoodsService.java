package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsBrand;

import java.util.List;

/**
 * (Goods)表服务接口
 *
 * @author Callay
 * @since 2024-01-30 19:05:04
 */
public interface GoodsService extends IService<Goods> {

    SaResult getRandomGoodsInfo();

    SaResult getGoodsByType(int type);

    SaResult getPageByType(int type, int page, int rows);

    SaResult getGoodsById(int id);

    SaResult getGoodsPageByState(int state,int page, int rows);
}


