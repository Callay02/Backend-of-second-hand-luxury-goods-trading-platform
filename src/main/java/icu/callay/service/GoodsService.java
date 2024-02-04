package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.Goods;

/**
 * (Goods)表服务接口
 *
 * @author makejava
 * @since 2024-01-30 19:05:04
 */
public interface GoodsService extends IService<Goods> {

    SaResult getRandomGoodsInfo();

    SaResult getGoodsByType(int type);
}


